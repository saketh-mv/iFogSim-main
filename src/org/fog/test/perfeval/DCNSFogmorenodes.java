package org.fog.test.perfeval;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.cloudbus.cloudsim.sdn.overbooking.BwProvisionerOverbooking;
import org.cloudbus.cloudsim.sdn.overbooking.PeProvisionerOverbooking;
import org.fog.application.AppEdge;
import org.fog.application.AppLoop;
import org.fog.application.Application;
import org.fog.application.selectivity.FractionalSelectivity;
import org.fog.entities.Actuator;
import org.fog.entities.FogBroker;
import org.fog.entities.FogDevice;
import org.fog.entities.FogDeviceCharacteristics;
import org.fog.entities.Sensor;
import org.fog.entities.Tuple;
import org.fog.placement.Controller;
import org.fog.placement.ModuleMapping;
import org.fog.placement.ModulePlacementEdgewards;
import org.fog.placement.ModulePlacementMapping;
import org.fog.policy.AppModuleAllocationPolicy;
import org.fog.scheduler.StreamOperatorScheduler;
import org.fog.utils.FogLinearPowerModel;
import org.fog.utils.FogUtils;
import org.fog.utils.TimeKeeper;
import org.fog.utils.distribution.DeterministicDistribution;

import com.mathworks.engine.MatlabEngine;

/**
 * Simulation setup for case study 2 - Intelligent Surveillance
 * @author Harshit Gupta
 *
 */
public class DCNSFogmorenodes {
	static ArrayList<FogDevice> fogDevices = new ArrayList<FogDevice>();
	static ArrayList<Sensor> sensors = new ArrayList<Sensor>();
	static ArrayList<Actuator> actuators = new ArrayList<Actuator>();
	static int numOfAreas = 10;
	static int numOfCamerasPerArea = 5;
	static int noofextranodes;
	static int cameras;
	static int numOfCamerasPerArea1=numOfCamerasPerArea/2;
	static Scanner sc=new Scanner(System.in);
	private static HashSet<Integer> h=new HashSet<Integer>();
	private static HashSet<Integer> h1=new HashSet<Integer>();
	private static HashSet<Integer> h2=new HashSet<Integer>();
	private static int[] nodes=new int[30];
	private static boolean CLOUD = false;
	private static boolean c;
	public static int totalnodes;


	
	public static void main(String[] args) throws Exception {
		
		Log.printLine("Starting DCNS...");
		while(true) {
			//System.out.println("gstgwtqqqqqqqqqqqqqqq"+"    ");
			sensors.removeAll(sensors);
			actuators.removeAll(actuators);
			fogDevices.removeAll(fogDevices);
			h.removeAll(h);
			h1.removeAll(h1);
			cameras=0;
			Thread.sleep(1000);
		
		 System.out.println("Enter no of extra nodes");
		 noofextranodes=sc.nextInt();
		 totalnodes=noofextranodes+numOfAreas;
		System.out.println("Enter true or false");
		 c=sc.nextBoolean();
		 for(int i=0;i<numOfAreas+noofextranodes;i++) {
			 if(i<numOfAreas) {
				 nodes[i]=5;
			 }
			 else {
				 nodes[i]=0;
			 }
		 }
		 
			Log.disable();
			int num_user = 1; // number of cloud users
			Calendar calendar = Calendar.getInstance();
			boolean trace_flag = false; // mean trace events

			CloudSim.init(num_user, calendar, trace_flag);

			String appId = "dcns"; // identifier of the application
			
			FogBroker broker = new FogBroker("broker");
			
			Application application = createApplication(appId, broker.getId());
			application.setUserId(broker.getId());
			
			createFogDevices(broker.getId(), appId);
			
			Controller controller = null;
			
			ModuleMapping moduleMapping = ModuleMapping.createModuleMapping(); // initializing a module mapping
			for(FogDevice device : fogDevices){
				if(device.getName().startsWith("m")){ // names of all Smart Cameras start with 'm' 
					moduleMapping.addModuleToDevice("motion_detector", device.getName());  // fixing 1 instance of the Motion Detector module to each Smart Camera
				}
			}
			moduleMapping.addModuleToDevice("user_interface", "cloud"); // fixing instances of User Interface module in the Cloud
			if(CLOUD){
				// if the mode of deployment is cloud-based
				moduleMapping.addModuleToDevice("object_detector", "cloud"); // placing all instances of Object Detector module in the Cloud
				moduleMapping.addModuleToDevice("object_tracker", "cloud"); // placing all instances of Object Tracker module in the Cloud
			}
			controller = new Controller("master-controller", fogDevices, sensors, 
					actuators);
			controller.submitApplication(application, 
					(CLOUD)?(new ModulePlacementMapping(fogDevices, application, moduleMapping))
							:(new ModulePlacementEdgewards(fogDevices, sensors, actuators, application, moduleMapping)));
			TimeKeeper.getInstance().setSimulationStartTime(Calendar.getInstance().getTimeInMillis());
			
			//System.out.println("utjrudjuejewww");
			int[] graph=new int[numOfAreas+noofextranodes];
			int[] graph1=new int[numOfAreas+noofextranodes];
			for(int j=0;j<numOfAreas+noofextranodes;j++) {
				graph[j]=j;
				graph1[j]=nodes[j];
			}
			MatlabEngine mat=MatlabEngine.startMatlab();
			Object handle=mat.feval("bar",graph,graph1);
			String s="id of each node";
			String s1="no of cameras working";
			String s2="Nodes Status";
			mat.feval("xlabel", s);
			mat.feval("ylabel", s1);
			mat.feval("title",s2);
			while((boolean)mat.feval("ishandle",handle)) {
	              Thread.sleep(500);
			}
			datatransferred(graph1);
			CloudSim.startSimulation();
		
			CloudSim.stopSimulation();
			
		}
	}
	
	
	private static void datatransferred(int[] graph1) throws IllegalArgumentException, IllegalStateException, InterruptedException, RejectedExecutionException, ExecutionException {
		// TODO Auto-generated method stub
		int d1=numOfAreas;
		int d2=d1;
		for(int d3=0;d3<numOfAreas+noofextranodes;d3++) {
		if(d3<d1 && graph1[d3]==0) {
		System.out.println("Failed node and all the nodes are transferred from "+d3+" to"+d2);
		d2++;
		int aulink=10000;
		aulink=aulink/1000;
		int adlink=30000;
		adlink=adlink/1000;
		int culink=4000;
		culink=culink/1000;
		int cdlink=10000;
		cdlink=cdlink/1000;
		int noofcams=7;
		double millis=7;
		int i=(int) millis;
		/*Frame rate-10=(input bitrate-5)/(time slots bits-0.5)*/
		System.out.println("Here we are assuming that data will be downloaded for every 2ms and uploaded every second "+aulink+"  "+adlink+"   "+i);
		int storagearea=0;
		ArrayList<Integer> storearea=new ArrayList<Integer>();
		int storagecam=0;
		ArrayList<Integer> storecam=new ArrayList<Integer>();
		ArrayList<Integer> varculink=new ArrayList<Integer>();
		ArrayList<Integer> varaulink=new ArrayList<Integer>();
		ArrayList<Integer> varadlink=new ArrayList<Integer>();
		ArrayList<Integer> ext=new ArrayList<Integer>();
		culink=culink+1;
		aulink=aulink+1;
		adlink=adlink+1;
		Random ran=new Random();
		while(true) {
			if(i>0) {
				int j1=ran.nextInt(culink);
				varculink.add(j1);
				storagecam=storagecam+cdlink-j1;
			    storecam.add(storagecam);
			    int j2=ran.nextInt(culink);
			    varculink.add(j2);
			    storagecam=storagecam-j2;
			    storecam.add(storagecam);
			    i=i-2;
			}
			else {
				int j3=ran.nextInt(culink);
				varculink.add(j3);
			    storagecam=storagecam-j3;
			    if(storagecam<=0) {
			    	break;
			    }
			    storecam.add(storagecam);
			}
		}
		int i1=storecam.size();
		int[] varculink1=new int[varculink.size()];
		int[] y3=new int[varculink.size()];
		int i3=0;
		for(int i2:varculink) {
			varculink1[i3]=i2;
			y3[i3]=i3;
			i3++;
		}
		int extstr=0;
		int time=varculink.size()/2;
		int t1=time;
		int i4=0;
		while(true) {
			if(varculink1[i4]==0) {
				storearea.add(0);
				i4++;
			}
			else {
				break;
			}
		}
		int k1=ran.nextInt(aulink);
		varaulink.add(k1);
		int q1=ran.nextInt(adlink);
		varadlink.add(q1);
		if(q1>noofcams*varculink1[i4]) {
			storagearea=storagearea+noofcams*varculink1[i4]-k1;
			if(storagearea<0) {
				storagearea=0;
			}
			storearea.add(storagearea);
		}
		else {
			extstr=extstr+noofcams*varculink1[i4]-q1;
			storagearea=storagearea+q1-k1;
			if(storagearea<0) {
				storagearea=0;
			}
			storearea.add(storagearea);
			ext.add(extstr);
		}
		i1--;
		i4++;
		while(true) {
			if(i1>0) {
				int k2=ran.nextInt(aulink);
				varaulink.add(k2);
				storagearea=storagearea-k2;
				if(storagearea<0) {
					storagearea=0;
				}
				storearea.add(storagearea);
				System.out.println(i4);
				extstr=extstr+noofcams*(varculink1[i4]+varculink1[i4+1]);
				ext.add(extstr);
				int k3=ran.nextInt(aulink);
				int q2=ran.nextInt(adlink);
				varaulink.add(k3);
				varadlink.add(q2);
				if(extstr>q2) {
					storagearea=storagearea+q2-k3;
					if(storagearea<0) {
						storagearea=0;
					}
					extstr=extstr-q2;
				}
				else {
					storagearea=storagearea+extstr-k3;
					if(storagearea<0) {
						storagearea=0;
					}
					extstr=0;
				}
			    storearea.add(storagearea);
				ext.add(extstr);
			    i1=i1-2;
			    i4=i4+2;
			    if(time>=i1) {
			    	System.out.println(time+"  "+i1+"  "+i4);
			    	break;
			    }
			}
		}
		int extstr1=0;
		while(true) {
			if(extstr>=0) {
				int k4=ran.nextInt(aulink);
				varaulink.add(k4);
				storagearea=storagearea-k4;
				if(storagearea<0) {
					storagearea=0;
				}
				storearea.add(storagearea);
				extstr1=extstr;
				int q3=ran.nextInt(adlink);
				varadlink.add(q3);
				extstr=extstr-q3;
				ext.add(extstr);
				System.out.println(extstr);
				int k5=ran.nextInt(aulink);
				varaulink.add(k5);
				if(extstr<0) {
					storagearea=storagearea+extstr1-k5;
				}
				else {
				storagearea=storagearea+q3-k5;
				}
				if(storagearea<0) {
					storagearea=0;
				}
			    storearea.add(storagearea);
			    //System.out.println(storagearea+"fffeeew");
			}
			else {
				break;
			}
		}
		while(true) {
			int k6=ran.nextInt(aulink);
			varaulink.add(k6);
		    storagearea=storagearea-k6;
		    if(storagearea<=0) {
		    	break;
				}
		    storearea.add(storagearea);
		    //System.out.println(storagearea+"    fff");
		}
		int[] uplinkarea=new int[varaulink.size()];
		int k=0;
		for(int a3:varaulink) {
			uplinkarea[k]=a3;
			k++;
		}
		double s1=0.7;
		double s2=0.3;
		double extstr2=0;//extra node
		double extstr3=0;//extra node
		double storagearea1=0;//extra node
		ArrayList<Double> storearea1=new ArrayList<Double>();//extra node
		ArrayList<Double> ext1=new ArrayList<Double>();//extra node
		ArrayList<Double> ext2=new ArrayList<Double>();//extra node
		ArrayList<Integer> varadlink1=new ArrayList<Integer>();
		ArrayList<Integer> varaulink1=new ArrayList<Integer>();
		
		while(true) {
			if(i1>0){
				System.out.println(i1+"value of i1");
				extstr2=extstr2+noofcams*(varculink1[i4]+varculink1[i4+1]);
				if(time<varaulink.size()) {
				extstr3=extstr3+uplinkarea[time];
				System.out.println(extstr3+"   -ve2");
				}
				ext1.add(extstr2);
				ext2.add(extstr3);
				int k3=ran.nextInt(aulink);
				int q2=ran.nextInt(adlink);
				varaulink1.add(k3);
				varadlink1.add(q2);
				if(extstr3<=0 && time>=varaulink.size()) {
					s1=1;
					s2=0;
				}
				if(extstr2>=q2*s1 && extstr3>=q2*s2) {
					storagearea1=storagearea1+q2-k3;
					if(storagearea1<0) {
						storagearea1=0;
					}
					extstr2=extstr2-q2*s1;
					extstr3=extstr3-q2*s2;
				}
				else if(extstr2>=q2*s1 && extstr3<q2*s2) {
					storagearea1=storagearea1+q2*s1+extstr3-k3;
					if(storagearea1<0) {
						storagearea1=0;
					}
					extstr2=extstr2-q2*s1;
					extstr3=0;
				}
				else if(extstr2<q2*s1 && extstr3>=q2*s2) {
					storagearea1=storagearea1+q2*s2+extstr2-k3;
					if(storagearea1<0) {
						storagearea1=0;
					}
					extstr2=0;
					extstr3=extstr3-q2*s2;
				}
				else {
					storagearea1=storagearea1+extstr3+extstr2-k3;
					if(storagearea1<0) {
						storagearea1=0;
					}
					extstr2=0;
					extstr3=0;
				}
			    storearea1.add(storagearea1);
				ext1.add(extstr2);
				ext2.add(extstr3);
			    i1=i1-2;
			    i4=i4+2;
			    time=time+1;
				int k2=ran.nextInt(aulink);
				varaulink1.add(k2);
				storagearea1=storagearea1-k2;
				if(storagearea1<0) {
					storagearea1=0;
				}
				storearea1.add(storagearea1);
				System.out.println(i4+"ueyeyeye");
			}
			else {
				break;
			}
		}
		while(true) {
			if(extstr2>=0) {
				int k4=ran.nextInt(aulink);
				varaulink1.add(k4);
				storagearea1=storagearea1-k4;
				if(storagearea1<0) {
					storagearea1=0;
				}
				storearea1.add(storagearea1);
				double extstr4 = extstr2;
				double extstr5=extstr3;	
				if(extstr3<=0 && time>=varaulink.size()) {
					s1=1;
					s2=0;
				}
				if(time<varaulink.size()) {
				extstr3=extstr3+uplinkarea[time];
				System.out.println(extstr3+"   -ve3");
				time++;
				}
				int q3=ran.nextInt(adlink);
				varadlink1.add(q3);
				extstr2=extstr2-q3*s1;
				ext1.add(extstr2);
				extstr3=extstr3-q3*s2;
				System.out.println(extstr3+"   -ve4");
				ext2.add(extstr3);
				if(extstr3<0) {
					extstr3=0;
				}
				System.out.println(extstr2);
				int k5=ran.nextInt(aulink);
				varaulink1.add(k5);
				if(extstr2<0 && extstr3<0) {
					storagearea1=storagearea1+extstr4+extstr5-k5;
				}
				else if(extstr2>=0 && extstr3<0){
				storagearea1=storagearea1+q3*s1+extstr5-k5;
				}
				else if(extstr2<0 && extstr3>=0) {
					storagearea1=storagearea1+q3*s2+extstr4-k5;
				}
				else {
					storagearea1=storagearea1+q3-k5;
				}
				if(storagearea1<0) {
					storagearea1=0;
				}
			    storearea1.add(storagearea1);
			}
			else {
				break;
			}
		}
		while(true) {
			if(extstr3>=0){
				int k4=ran.nextInt(aulink);
				varaulink1.add(k4);
				storagearea1=storagearea1-k4;
				if(storagearea1<0) {
					storagearea1=0;
				}
				storearea1.add(storagearea1);
				double extstr4 = extstr3;
				int q3=ran.nextInt(adlink);
				varadlink1.add(q3);
				extstr3=extstr3-q3;
				System.out.println(extstr3+"   -ve1");
				ext2.add(extstr3);
				int k5=ran.nextInt(aulink);
				varaulink1.add(k5);
				if(extstr3<0) {
					storagearea1=storagearea1+extstr4-k5;
				}
				else {
				storagearea1=storagearea1+q3-k5;
				}
				if(storagearea1<0) {
					storagearea1=0;
				}
			    storearea1.add(storagearea1);
			}
			else {
				break;
			}
		}
		while(true) {
			int k6=ran.nextInt(aulink);
			varaulink1.add(k6);
		    storagearea1=storagearea1-k6;
		    if(storagearea1<=0) {
		    	break;
				}
		    storearea1.add(storagearea1);
		    //System.out.println(storagearea+"    fff");
		}
		System.out.println(storecam+"----->camera storage");
		System.out.println(storearea+"------>area storage");
		System.out.println(ext+"------->extstr");
		System.out.println(varculink+"------>uplink variations for camera node");
		System.out.println(varaulink+"------>uplink variations for area node");
		System.out.println(varadlink+"------>downlink variations for area node");
		System.out.println(ext1+"------->extstr2");
		System.out.println(ext2+"------->extstr3");
		System.out.println(storearea1+"------>area storage");
		System.out.println(varaulink1+"------>uplink variations for area node");
		System.out.println(varadlink1+"------>downlink variations for area node");
		/*MatlabEngine mat1=MatlabEngine.startMatlab();
		MatlabEngine mat2=MatlabEngine.startMatlab();
		MatlabEngine mat3=MatlabEngine.startMatlab();
		MatlabEngine mat4=MatlabEngine.startMatlab();
		MatlabEngine mat5=MatlabEngine.startMatlab();
		MatlabEngine mat6=MatlabEngine.startMatlab();
		MatlabEngine mat7=MatlabEngine.startMatlab();
		MatlabEngine mat8=MatlabEngine.startMatlab();
		char a='-';
		double[] camera=new double[storecam.size()];
		double[] area=new double[storearea.size()];
		//int[] uplinkcam=new int[varculink.size()];
		int[] downlinkarea=new int[2*varadlink.size()];
		int[] uplinkarea1=new int[varaulink.size()];
		int[] downlinkarea1=new int[2*varadlink1.size()+t1];
		int[] uplinkarea2=new int[varaulink1.size()+t1];
		double[] area1=new double[storearea1.size()+t1];
		int[] y1=new int[storecam.size()];
		int[] y2=new int[storearea.size()];
		int[] y4=new int[2*varadlink.size()];
		int[] y5=new int[varaulink.size()];
		int[] y6=new int[storearea1.size()+t1];
		int[] y7=new int[2*varadlink1.size()+t1];
		int[] y8=new int[varaulink1.size()+t1];
		int k11=0;
		for(double a3:storecam) {
			camera[k11]=a3/8;
			y1[k11]=k11;
			k11++;
		}
		k11=0;
		for(double a3:storearea) {
			area[k11]=a3/8;
			y2[k11]=k11;
			k11++;
		}
		k11=0;
		for(int a3:varadlink) {
			downlinkarea[k11]=a3;
			k11=k11+2;
		}
		k11=0;
		for(int a3:varadlink) {
			y4[k11]=k11;
			k11++;
			y4[k11]=k11;
			k11++;
		}
		k11=0;
		for(int a3:varaulink) {
			uplinkarea1[k11]=a3;
			y5[k11]=k11;
			k11++;
		}
		k11=t1;
		for(int a3:varadlink1) {
				downlinkarea1[k11]=a3;
				k11=k11+2;
		}
		for(int k12=0;k12<2*varadlink1.size()+t1;k12++) {
			y7[k12]=k12;
		}
		k11=t1;
		for(int a3:varaulink1) {
				uplinkarea2[k11]=a3;
				k1++;
		}
		for(int k12=0;k12<varaulink1.size()+t1;k12++) {
			y8[k12]=k12;
		}
		k11=t1;
		for(double a3:storearea1) {
				area1[k11]=a3/8;
				k11++;
		}
		for(int k12=0;k12<storearea1.size()+t1;k12++) {
			y6[k12]=k12;
		}
		Object handle1=mat1.feval("plot",y1,camera,a);
		Object handle2=mat2.feval("plot",y2,area,a);
		Object handle3=mat3.feval("plot",y3,varculink1,a);
		Object handle4=mat4.feval("plot",y4,downlinkarea,a);
		Object handle5=mat5.feval("plot",y5,uplinkarea1,a);
		Object handle6=mat6.feval("plot",y6,area1,a);
		Object handle7=mat7.feval("plot",y7,downlinkarea1,a);
		Object handle8=mat8.feval("plot",y8,uplinkarea2,a);
		String s="Data stored in camera node";
		String st1="Time in ms";
		String st2="Data storage(in Bytes)";
		String s3="Data stored in area node";
		String s4="Time in ms";
		String s5="Data storage(in Bytes)";
		String s6="Band-width";
		String s7="Time in ms";
		String s8="Bandwidth variations of camera uplink";
		String s9="Band-width";
		String s10="Time in ms";
		String s11="Bandwidth variations of area downlink";
		String s12="Band-width";
		String s13="Time in ms";
		String s14="Bandwidth variations of area uplink";
		String s15="Data stored in extranode";
		String s16="Time in ms";
		String s17="Data Storage(in Bytes)";
		String s18="Band-width";
		String s19="Time in ms";
		String s20="Bandwidth variations of area downlink(extra node)";
		String s21="Band-width";
		String s22="Time in ms";
		String s23="Bandwidth variations of area uplink(extra node)";
		mat1.feval("xlabel",st1);
		mat1.feval("ylabel",s);
		mat1.feval("title",st2);
		mat2.feval("xlabel",s4);
		mat2.feval("ylabel",s3);
		mat2.feval("title",s5);
		mat3.feval("xlabel",s7);
		mat3.feval("ylabel",s6);
		mat3.feval("title",s8);
		mat4.feval("xlabel",s10);
		mat4.feval("ylabel",s9);
		mat4.feval("title",s11);
		mat5.feval("xlabel",s13);
		mat5.feval("ylabel",s12);
		mat5.feval("title",s14);
		mat6.feval("xlabel",s16);
		mat6.feval("ylabel",s15);
		mat6.feval("title",s17);
		mat7.feval("xlabel",s19);
		mat7.feval("ylabel",s18);
		mat7.feval("title",s20);
		mat8.feval("xlabel",s22);
		mat8.feval("ylabel",s21);
		mat8.feval("title",s23);
		while((boolean)mat1.feval("ishandle",handle1)) {
            Thread.sleep(5000);
            }
		while((boolean)mat2.feval("ishandle",handle2)) {
			Thread.sleep(5000);
			}
		while((boolean)mat3.feval("ishandle",handle3)) {
			Thread.sleep(5000);
			}
		while((boolean)mat4.feval("ishandle",handle4)) {
			Thread.sleep(5000);
			}
		while((boolean)mat5.feval("ishandle",handle5)) {
			Thread.sleep(5000);
			}
		while((boolean)mat6.feval("ishandle",handle6)) {
			Thread.sleep(5000);
			}
		while((boolean)mat7.feval("ishandle",handle7)) {
			Thread.sleep(5000);
			}
		while((boolean)mat8.feval("ishandle",handle8)) {
			Thread.sleep(5000);
			}*/
		}
		else if(d3<d1 && graph1[d3]==numOfCamerasPerArea) {
			System.out.println("Working node and no cameras will be transferred");
			int aulink=10000;
			aulink=aulink/1000;
			int adlink=30000;
			adlink=adlink/1000;
			int culink=4000;
			culink=culink/1000;
			int cdlink=10000;
			cdlink=cdlink/1000;
			int noofcams=9;
			double millis=10;
			int i=(int) millis;
			System.out.println("Here we are assuming that data will be downloaded for every 2ms and uploaded every second "+aulink+"  "+adlink+"   "+i);
			int storagearea=0;
			ArrayList<Integer> storearea=new ArrayList<Integer>();
			int storagecam=0;
			ArrayList<Integer> storecam=new ArrayList<Integer>();
			ArrayList<Integer> varculink=new ArrayList<Integer>();
			ArrayList<Integer> varaulink=new ArrayList<Integer>();
			ArrayList<Integer> varadlink=new ArrayList<Integer>();
			ArrayList<Integer> ext=new ArrayList<Integer>();
			culink=culink+1;
			aulink=aulink+1;
			adlink=adlink+1;
			Random ran=new Random();
			while(true) {
				if(i>0) {
					int j1=ran.nextInt(culink);
					varculink.add(j1);
					storagecam=storagecam+cdlink-j1;
				    storecam.add(storagecam);
				    int j2=ran.nextInt(culink);
				    varculink.add(j2);
				    storagecam=storagecam-j2;
				    storecam.add(storagecam);
				    i=i-2;
				}
				else {
					int j3=ran.nextInt(culink);
					varculink.add(j3);
				    storagecam=storagecam-j3;
				    if(storagecam<=0) {
				    	break;
				    }
				    storecam.add(storagecam);
				}
			}
			int i1=storecam.size();
			int[] varculink1=new int[varculink.size()];
			int[] y3=new int[varculink.size()];
			int i3=0;
			for(int i2:varculink) {
				varculink1[i3]=i2;
				y3[i3]=i3;
				i3++;
			}
			int extstr=0;
			int i4=0;
			while(true) {
				if(varculink1[i4]==0) {
					storearea.add(0);
					i4++;
				}
				else {
					break;
				}
			}
			int k1=ran.nextInt(aulink);
			varaulink.add(k1);
			int q1=ran.nextInt(adlink);
			varadlink.add(q1);
			if(q1>noofcams*varculink1[i4]) {
				storagearea=storagearea+noofcams*varculink1[i4]-k1;
				if(storagearea<0) {
					storagearea=0;
				}
				storearea.add(storagearea);
			}
			else {
				extstr=extstr+noofcams*varculink1[i4]-q1;
				storagearea=storagearea+q1-k1;
				if(storagearea<0) {
					storagearea=0;
				}
				storearea.add(storagearea);
				ext.add(extstr);
			}
			i1--;
			i4++;
			while(true) {
				if(i1>0) {
					int k2=ran.nextInt(aulink);
					varaulink.add(k2);
					storagearea=storagearea-k2;
					if(storagearea<0) {
						storagearea=0;
					}
					storearea.add(storagearea);
					System.out.println(i4);
					extstr=extstr+noofcams*(varculink1[i4]+varculink1[i4+1]);
					ext.add(extstr);
					int k3=ran.nextInt(aulink);
					int q2=ran.nextInt(adlink);
					varaulink.add(k3);
					varadlink.add(q2);
					if(extstr>q2) {
						storagearea=storagearea+q2-k3;
						if(storagearea<0) {
							storagearea=0;
						}
						extstr=extstr-q2;
					}
					else {
						storagearea=storagearea+extstr-k3;
						if(storagearea<0) {
							storagearea=0;
						}
						extstr=0;
					}
				    storearea.add(storagearea);
					ext.add(extstr);
				    i1=i1-2;
				    i4=i4+2;
				}
				else {
					break;
				}
			}
			int extstr1=0;
			while(true) {
				if(extstr>=0) {
					int k4=ran.nextInt(aulink);
					varaulink.add(k4);
					storagearea=storagearea-k4;
					if(storagearea<0) {
						storagearea=0;
					}
					storearea.add(storagearea);
					extstr1=extstr;
					int q3=ran.nextInt(adlink);
					varadlink.add(q3);
					extstr=extstr-q3;
					ext.add(extstr);
					System.out.println(extstr);
					int k5=ran.nextInt(aulink);
					varaulink.add(k5);
					if(extstr<0) {
						storagearea=storagearea+extstr1-k5;
					}
					else {
					storagearea=storagearea+q3-k5;
					}
					if(storagearea<0) {
						storagearea=0;
					}
				    storearea.add(storagearea);
				    //System.out.println(storagearea+"fffeeew");
				}
				else {
					break;
				}
			}
			while(true) {
				int k6=ran.nextInt(aulink);
				varaulink.add(k6);
			    storagearea=storagearea-k6;
			    if(storagearea<=0) {
			    	break;
					}
			    storearea.add(storagearea);
			    //System.out.println(storagearea+"    fff");
			}
			System.out.println(storecam+"----->camera storage");
			System.out.println(storearea+"------>area storage");
			System.out.println(ext+"------->extstr");
			System.out.println(varculink+"------>uplink variations for camera node");
			System.out.println(varaulink+"------>uplink variations for area node");
			System.out.println(varadlink+"------>downlink variations for area node");
			MatlabEngine mat1=MatlabEngine.startMatlab();
			MatlabEngine mat2=MatlabEngine.startMatlab();
			MatlabEngine mat3=MatlabEngine.startMatlab();
			MatlabEngine mat4=MatlabEngine.startMatlab();
			MatlabEngine mat5=MatlabEngine.startMatlab();
			char a='-';
			double[] camera=new double[storecam.size()];
			double[] area=new double[storearea.size()];
			//int[] uplinkcam=new int[varculink.size()];
			int[] downlinkarea=new int[2*varadlink.size()];
			int[] uplinkarea=new int[varaulink.size()];
			int[] y1=new int[storecam.size()];
			int[] y2=new int[storearea.size()];
			int[] y4=new int[2*varadlink.size()];
			int[] y5=new int[varaulink.size()];
			int k=0;
			for(double a3:storecam) {
				camera[k]=a3/8;
				y1[k]=k;
				k++;
			}
			k=0;
			for(double a3:storearea) {
				area[k]=a3/8;
				y2[k]=k;
				k++;
			}
			k=0;
			for(int a3:varadlink) {
				downlinkarea[k]=a3;
				y4[k]=k;
				k++;
				y4[k]=k;
				k++;
			}
			k=0;
			for(int a3:varaulink) {
				uplinkarea[k]=a3;
				y5[k]=k;
				k++;
			}
			Object handle1=mat1.feval("plot",y1,camera,a);
			Object handle2=mat2.feval("plot",y2,area,a);
			Object handle3=mat3.feval("plot",y3,varculink1,a);
			Object handle4=mat4.feval("plot",y4,downlinkarea,a);
			Object handle5=mat5.feval("plot",y5,uplinkarea,a);
			String s="Data stored in camera node";
			String s1="Time in ms";
			String s2="Data storage(in Bytes)";
			String s3="Data stored in area node";
			String s4="Time in ms";
			String s5="Data storage(in Bytes)";
			String s6="Band-width";
			String s7="Time in ms";
			String s8="Bandwidth variations of camera uplink";
			String s9="Band-width";
			String s10="Time in ms";
			String s11="Bandwidth variations of area downlink";
			String s12="Band-width";
			String s13="Time in ms";
			String s14="Bandwidth variations of area uplink";
			mat1.feval("xlabel",s1);
			mat1.feval("ylabel",s);
			mat1.feval("title",s2);
			mat2.feval("xlabel",s4);
			mat2.feval("ylabel",s3);
			mat2.feval("title",s5);
			mat3.feval("xlabel",s7);
			mat3.feval("ylabel",s6);
			mat3.feval("title",s8);
			mat4.feval("xlabel",s10);
			mat4.feval("ylabel",s9);
			mat4.feval("title",s11);
			mat5.feval("xlabel",s13);
			mat5.feval("ylabel",s12);
			mat5.feval("title",s14);
			while((boolean)mat1.feval("ishandle",handle1)) {
	            Thread.sleep(5000);
	            }
			while((boolean)mat2.feval("ishandle",handle2)) {
				Thread.sleep(5000);
				}
			while((boolean)mat3.feval("ishandle",handle3)) {
				Thread.sleep(5000);
				}
			while((boolean)mat4.feval("ishandle",handle4)) {
				Thread.sleep(5000);
				}
			while((boolean)mat5.feval("ishandle",handle5)) {
				Thread.sleep(5000);
				}
		}
		}
	}
		
	
	/**
	 * Creates the fog devices in the physical topology of the simulation.
	 * @param userId
	 * @param appId
	 */
	private static void createFogDevices(int userId, String appId) {
		FogDevice cloud = createFogDevice("cloud", 44800, 40000, 100, 10000, 0, 0.01, 16*103, 16*83.25);
		cloud.setParentId(-1);
		fogDevices.add(cloud);
		FogDevice proxy = createFogDevice("proxy-server", 2800, 4000, 10000, 10000, 1, 0.0, 107.339, 83.4333);
		proxy.setParentId(cloud.getId());
		proxy.setUplinkLatency(100); // latency of connection between proxy server and cloud is 100 ms
		fogDevices.add(proxy);
		int i;
		int k=0;
		int j = -1;
		if(c==true) {
		Random ran=new Random();
		int j1=ran.nextInt(numOfAreas);
		//j1=0;
		j1=7;
		System.out.println("no of nodes failed--> "+j1);
		for(int i1=0;i1<j1;) {
		j=ran.nextInt(numOfAreas);
		if(h.contains(j*j*j)!=true) {
			i1++;
		}
		h.add(j*j*j);
		//System.out.println(j+"     failed "+i1);
		}
		}
		//System.out.println(j);
		System.out.println("Hashset            "+h);
		Random ran1=new Random();
		int j3=1;
		if(c==true) {
			j3=h.size();
		}
		int j1=0;
		if(h.size()!=0) j1=ran1.nextInt(j3);//no of  fully failed nodes
		if(c==true && j3!=0) System.out.println("no of fully failed nodes--> "+j1);
		int l1=0;
		int l2=0;
		j1=4;
		for(int h3:h) {
			if(l2>=j1) {
					h2.add(h3);
				}
	           else {
	        	   h1.add(h3);
	           }
			l2++;
		}
		System.out.println("Hashset1            "+h1);
		System.out.println("Hashset2            "+h2);
		
			for(i=0;i<numOfAreas;i++){
				if(c==true &&( h1.contains(i*i*i)==true||h2.contains(i*i*i)==true)) {
					k=proxy.getId();
					//l1++;
					//System.out.println("l1    "+l1);
					if( h1.contains(i*i*i)==true) {
						System.out.println("Node "+i+" fully failed");
						nodes[i]=0;
						continue;
					}
					else if(h2.contains(i*i*i)==true) {
						System.out.println("Node "+i+" partially failed");
						nodes[i]=numOfCamerasPerArea1;
						addArea(i+"", userId, appId, proxy.getId(),0);
						continue;
					}
				}
				addArea(i+"", userId, appId, proxy.getId(),1);
			}
			if(c==true) {
				for(int j2=0;j2<j1;j2++) {
					System.out.println(i+"   related to fully nodes");
					nodes[i]=numOfCamerasPerArea;
				addArea1(i+"", userId, appId, k);
				i++;
				}
				if(h2.size()!=0) {
					int q=cameras/numOfCamerasPerArea;//no of nodes required
					if(cameras%numOfCamerasPerArea!=0) {
						q=q+1;
					}
					System.out.println(q+"       no of nodes required");
						int q1=cameras;
						int q2=(noofextranodes-h1.size());
						for(int q3=0;q3<(noofextranodes-h1.size());q3++){
							System.out.println("Extra node for cameras due to partial failed nodes"+i);
							if(q1/q2!=0) {
								addArea2(i+"", userId, appId, k,q1/q2);
								nodes[i]=q1/q2;
							}
							q1=q1-q1/q2;
							q2=q2-1;
							i++;
						}
				}
			}
			else {
				for(int j2=0;j2<noofextranodes;j2++) {
					addArea1(i+"", userId, appId, k);
					i++;
					}
			}
	}

	private static void addArea2(String id, int userId, String appId, int parentId, int i1) {
		// TODO Auto-generated method stub
		FogDevice router = createFogDevice("d-"+id, 2800, 4000, 10000, 10000, 1, 0.0, 107.339, 83.4333);
		fogDevices.add(router);
		router.setUplinkLatency(2); // latency of connection between router and proxy server is 2 ms
		for(int i=0;i<i1;i++){
			String mobileId = id+"-"+i;
			FogDevice camera = addCamera(mobileId, userId, appId, router.getId()); // adding a smart camera to the physical topology. Smart cameras have been modeled as fog devices as well.
			camera.setUplinkLatency(2); // latency of connection between camera and router is 2 ms
			fogDevices.add(camera);
		}
		router.setParentId(parentId);
		return;
	}

	private static void addArea(String id, int userId, String appId, int parentId,int n){
		FogDevice router = createFogDevice("d-"+id, 2800, 4000, 10000, 10000, 1, 0.0, 107.339, 83.4333);
		fogDevices.add(router);
		router.setUplinkLatency(2); // latency of connection between router and proxy server is 2 ms
		if(n==1) {
			for(int i=0;i<numOfCamerasPerArea;i++){
			String mobileId = id+"-"+i;
			FogDevice camera = addCamera(mobileId, userId, appId, router.getId()); // adding a smart camera to the physical topology. Smart cameras have been modeled as fog devices as well.
			camera.setUplinkLatency(2); // latency of connection between camera and router is 2 ms
			fogDevices.add(camera);
		}
		}
		else if(n==0) {
			for(int i=0;i<numOfCamerasPerArea1;i++){
				String mobileId = id+"-"+i;
				FogDevice camera = addCamera(mobileId, userId, appId, router.getId()); // adding a smart camera to the physical topology. Smart cameras have been modeled as fog devices as well.
				camera.setUplinkLatency(2); // latency of connection between camera and router is 2 ms
				fogDevices.add(camera);
			}
			cameras=cameras+numOfCamerasPerArea-numOfCamerasPerArea1;
			System.out.println("Extra cameras from partially failed nodes-->"+cameras);
		}
		router.setParentId(parentId);
		return;
	}
	private static void addArea1(String id, int userId, String appId, int parentId){
		if(parentId!=0) {
		FogDevice router = createFogDevice("d-"+id, 2800, 5000, 11000, 11000, 1, 0.0, 107.339, 83.4333);
		fogDevices.add(router);
		int z=sc.nextInt();
		int z1=sc.nextInt();
		router.setUplinkLatency(z); // latency of connection between router and proxy server is 2 ms
		for(int i=0;i<numOfCamerasPerArea;i++){
			String mobileId = id+"-"+i;
			FogDevice camera = addCamera(mobileId, userId, appId, router.getId()); // adding a smart camera to the physical topology. Smart cameras have been modeled as fog devices as well.
			camera.setUplinkLatency(z1); // latency of connection between camera and router is 2 ms
			fogDevices.add(camera);
		}
		router.setParentId(parentId);
		}
		else {
			System.out.println("Extra module has been created at id "+id);
		}
		return;
	}
	private static FogDevice addCamera(String id, int userId, String appId, int parentId){
		FogDevice camera = createFogDevice("m-"+id, 500, 1000, 10000, 10000, 3, 0, 87.53, 82.44);
		camera.setParentId(parentId);
		Sensor sensor = new Sensor("s-"+id, "CAMERA", userId, appId, new DeterministicDistribution(5)); // inter-transmission time of camera (sensor) follows a deterministic distribution
		sensors.add(sensor);
		Actuator ptz = new Actuator("ptz-"+id, userId, appId, "PTZ_CONTROL");
		actuators.add(ptz);
		sensor.setGatewayDeviceId(camera.getId());
		sensor.setLatency(1.0);  // latency of connection between camera (sensor) and the parent Smart Camera is 1 ms
		ptz.setGatewayDeviceId(camera.getId());
		ptz.setLatency(1.0);  // latency of connection between PTZ Control and the parent Smart Camera is 1 ms
		return camera;
	}
	
	/**
	 * Creates a vanilla fog device
	 * @param nodeName name of the device to be used in simulation
	 * @param mips MIPS
	 * @param ram RAM
	 * @param upBw uplink bandwidth
	 * @param downBw downlink bandwidth
	 * @param level hierarchy level of the device
	 * @param ratePerMips cost rate per MIPS used
	 * @param busyPower
	 * @param idlePower
	 * @return
	 */
	private static FogDevice createFogDevice(String nodeName, long mips,
			int ram, long upBw, long downBw, int level, double ratePerMips, double busyPower, double idlePower) {
		
		List<Pe> peList = new ArrayList<Pe>();

		// 3. Create PEs and add these into a list.
		peList.add(new Pe(0, new PeProvisionerOverbooking(mips))); // need to store Pe id and MIPS Rating

		int hostId = FogUtils.generateEntityId();
		long storage = 1000000; // host storage
		int bw = 10000;

		PowerHost host = new PowerHost(
				hostId,
				new RamProvisionerSimple(ram),
				new BwProvisionerOverbooking(bw),
				storage,
				peList,
				new StreamOperatorScheduler(peList),
				new FogLinearPowerModel(busyPower, idlePower)
			);

		List<Host> hostList = new ArrayList<Host>();
		hostList.add(host);

		String arch = "x86"; // system architecture
		String os = "Linux"; // operating system
		String vmm = "Xen";
		double time_zone = 10.0; // time zone this resource located
		double cost = 3.0; // the cost of using processing in this resource
		double costPerMem = 0.05; // the cost of using memory in this resource
		double costPerStorage = 0.001; // the cost of using storage in this
										// resource
		double costPerBw = 0.0; // the cost of using bw in this resource
		LinkedList<Storage> storageList = new LinkedList<Storage>(); // we are not adding SAN
													// devices by now

		FogDeviceCharacteristics characteristics = new FogDeviceCharacteristics(
				arch, os, vmm, host, time_zone, cost, costPerMem,
				costPerStorage, costPerBw);

		FogDevice fogdevice = null;
		FogDevice fg=null; 
		try {
			fogdevice = new FogDevice(nodeName, characteristics, 
					new AppModuleAllocationPolicy(hostList), storageList, 10, upBw, downBw, 0, ratePerMips);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		fogdevice.setLevel(level);
		return fogdevice;
	}

	/**
	 * Function to create the Intelligent Surveillance application in the DDF model. 
	 * @param appId unique identifier of the application
	 * @param userId identifier of the user of the application
	 * @return
	 */
	@SuppressWarnings({"serial" })
	private static Application createApplication(String appId, int userId){
		
		Application application = Application.createApplication(appId, userId);
		/*
		 * Adding modules (vertices) to the application model (directed graph)
		 */
		application.addAppModule("object_detector", 10);
		application.addAppModule("motion_detector", 10);
		application.addAppModule("object_tracker", 10);
		application.addAppModule("user_interface", 10);
		
		/*
		 * Connecting the application modules (vertices) in the application model (directed graph) with edges
		 */
		application.addAppEdge("CAMERA", "motion_detector", 1000, 20000, "CAMERA", Tuple.UP, AppEdge.SENSOR); // adding edge from CAMERA (sensor) to Motion Detector module carrying tuples of type CAMERA
		application.addAppEdge("motion_detector", "object_detector", 2000, 2000, "MOTION_VIDEO_STREAM", Tuple.UP, AppEdge.MODULE); // adding edge from Motion Detector to Object Detector module carrying tuples of type MOTION_VIDEO_STREAM
		application.addAppEdge("object_detector", "user_interface", 500, 2000, "DETECTED_OBJECT", Tuple.UP, AppEdge.MODULE); // adding edge from Object Detector to User Interface module carrying tuples of type DETECTED_OBJECT
		application.addAppEdge("object_detector", "object_tracker", 1000, 100, "OBJECT_LOCATION", Tuple.UP, AppEdge.MODULE); // adding edge from Object Detector to Object Tracker module carrying tuples of type OBJECT_LOCATION
		application.addAppEdge("object_tracker", "PTZ_CONTROL", 100, 28, 100, "PTZ_PARAMS", Tuple.DOWN, AppEdge.ACTUATOR); // adding edge from Object Tracker to PTZ CONTROL (actuator) carrying tuples of type PTZ_PARAMS
		
		/*
		 * Defining the input-output relationships (represented by selectivity) of the application modules. 
		 */
		application.addTupleMapping("motion_detector", "CAMERA", "MOTION_VIDEO_STREAM", new FractionalSelectivity(1.0)); // 1.0 tuples of type MOTION_VIDEO_STREAM are emitted by Motion Detector module per incoming tuple of type CAMERA
		application.addTupleMapping("object_detector", "MOTION_VIDEO_STREAM", "OBJECT_LOCATION", new FractionalSelectivity(1.0)); // 1.0 tuples of type OBJECT_LOCATION are emitted by Object Detector module per incoming tuple of type MOTION_VIDEO_STREAM
		application.addTupleMapping("object_detector", "MOTION_VIDEO_STREAM", "DETECTED_OBJECT", new FractionalSelectivity(0.05)); // 0.05 tuples of type MOTION_VIDEO_STREAM are emitted by Object Detector module per incoming tuple of type MOTION_VIDEO_STREAM
	    
		/*
		 * Defining application loops (maybe incomplete loops) to monitor the latency of. 
		 * Here, we add two loops for monitoring : Motion Detector -> Object Detector -> Object Tracker and Object Tracker -> PTZ Control
		 */
		final AppLoop loop1 = new AppLoop(new ArrayList<String>(){{add("CAMERA");add("motion_detector");add("object_detector");add("user_interface");}});
		final AppLoop loop2 = new AppLoop(new ArrayList<String>(){{add("object_tracker");add("PTZ_CONTROL");}});
		List<AppLoop> loops = new ArrayList<AppLoop>(){{add(loop1);add(loop2);}};
		
		application.setLoops(loops);
		return application;
	}

	public static int getnodes() {
		// TODO Auto-generated method stub
		return totalnodes;
	}
}