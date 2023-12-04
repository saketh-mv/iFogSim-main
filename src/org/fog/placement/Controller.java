package org.fog.placement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import org.fog.application.AppEdge;
import org.fog.application.AppLoop;
import org.fog.application.AppModule;
import org.fog.application.Application;
import org.fog.entities.Actuator;
import org.fog.entities.FogDevice;
import org.fog.entities.Sensor;
import org.fog.utils.Config;
import org.fog.utils.FogEvents;
import org.fog.utils.FogUtils;
import org.fog.utils.NetworkUsageMonitor;
import org.fog.utils.TimeKeeper;
import org.fog.test.perfeval.DCNSFog_original;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;
import java.util.concurrent.ExecutionException;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;

public class Controller extends SimEntity{
	
	public static boolean ONLY_CLOUD = false;
		
	private List<FogDevice> fogDevices;
	private List<Sensor> sensors;
	private List<Actuator> actuators;
	
	private Map<String, Application> applications;
	private Map<String, Integer> appLaunchDelays;

	private Map<String, ModulePlacement> appModulePlacementPolicy;
	private ArrayList<Double> energy1=new ArrayList<Double>();
	private double[] allenergies=new double[30];
	private int nodes;
	private static MatlabEngine mat;
	private static MatlabEngine mat1;
	private Calendar calndr1 = Calendar.getInstance();
	float a1=calndr1.getTimeInMillis();
	
	public Controller(String name, List<FogDevice> fogDevices, List<Sensor> sensors, List<Actuator> actuators) {
		super(name);
		this.applications = new HashMap<String, Application>();
		setAppLaunchDelays(new HashMap<String, Integer>());
		setAppModulePlacementPolicy(new HashMap<String, ModulePlacement>());
		for(FogDevice fogDevice : fogDevices){
			fogDevice.setControllerId(getId());
		}
		setFogDevices(fogDevices);
		setActuators(actuators);
		setSensors(sensors);
		connectWithLatencies();
	}

	private FogDevice getFogDeviceById(int id){
		for(FogDevice fogDevice : getFogDevices()){
			if(id==fogDevice.getId())
				return fogDevice;
		}
		return null;
	}
	
	private void connectWithLatencies(){
		for(FogDevice fogDevice : getFogDevices()){
			FogDevice parent = getFogDeviceById(fogDevice.getParentId());
			if(parent == null)
				continue;
			double latency = fogDevice.getUplinkLatency();
			parent.getChildToLatencyMap().put(fogDevice.getId(), latency);
			parent.getChildrenIds().add(fogDevice.getId());
		}
	}
	
	@Override
	public void startEntity() {
		for(String appId : applications.keySet()){
			if(getAppLaunchDelays().get(appId)==0)
				processAppSubmit(applications.get(appId));
			else
				send(getId(), getAppLaunchDelays().get(appId), FogEvents.APP_SUBMIT, applications.get(appId));
		}

		send(getId(), Config.RESOURCE_MANAGE_INTERVAL, FogEvents.CONTROLLER_RESOURCE_MANAGE);
		
		send(getId(), Config.MAX_SIMULATION_TIME, FogEvents.STOP_SIMULATION);
		
		for(FogDevice dev : getFogDevices())
			sendNow(dev.getId(), FogEvents.RESOURCE_MGMT);
		
		System.out.println("yehyebdyewywdj");
	}
	
	@Override
	public void processEvent(SimEvent ev)  {
		//System.out.println("       ASdjieooialkma         "+ev);
		switch(ev.getTag()){
		case FogEvents.APP_SUBMIT:
			processAppSubmit(ev);
			break;
		case FogEvents.TUPLE_FINISHED:
			processTupleFinished(ev);
			break;
		case FogEvents.CONTROLLER_RESOURCE_MANAGE:
			manageResources();
			break;
		case FogEvents.STOP_SIMULATION:
			//CloudSim.stopSimulation();
			printTimeDetails();
			printPowerDetails();
			printCostDetails();
			printNetworkUsageDetails();
			try {
				plotgraph();
			} catch (MatlabExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MatlabSyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*try {
				datatransmission();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RejectedExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			System.out.println("Not stopped1");
			Calendar calndr2 = Calendar.getInstance();
			//float a3=calndr2.getTimeInMillis()-calndr1.getTimeInMillis();
			//System.out.println(a3);
			System.out.println(calndr2.getTimeInMillis()-calndr1.getTimeInMillis()+"   "+calndr2.getTimeInMillis()+"    "+calndr1.getTimeInMillis() );
			System.exit(0);
			System.out.println("Not stopped");
			break;
			
		}
	}
	
	private void datatransmission() throws IllegalArgumentException, IllegalStateException, InterruptedException, RejectedExecutionException, ExecutionException {
		// TODO Auto-generated method stub
		int aulink=(int) DCNSFog_original.areauplink;
		aulink=aulink/1000;
		int adlink=(int) DCNSFog_original.areadownlink;
		adlink=adlink/1000;
		int culink=(int) DCNSFog_original.camerauplink;
		culink=culink/1000;
		int cdlink=(int) DCNSFog_original.cameradownlink;
		cdlink=cdlink/1000;
		int noofcams=DCNSFog_original.numOfCamerasPerArea;
		double millis=FogDevice.l1;
		int i=(int) millis;
		System.out.println("Here we are assuming that data will be downloaded for every 2ms and uploaded every second "+aulink+"  "+adlink+"   "+i);
		int storagearea=0;
		ArrayList<Integer> storearea=new ArrayList<Integer>();
		int storagecam=0;
		ArrayList<Integer> storecam=new ArrayList<Integer>();
		/*ArrayList<Integer> varculink=new ArrayList<Integer>();
		//ArrayList<Integer> varadlink=new ArrayList<Integer>();
		culink=culink+1;
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
				i4++;
			}
			else {
				break;
			}
		}
		if(adlink>noofcams*varculink1[i4]) {
			storagearea=storagearea+noofcams*varculink1[i4]-aulink;
			if(storagearea<0) {
				storagearea=0;
			}
			storearea.add(storagearea);
		}
		else {
			extstr=extstr+noofcams*varculink1[i4]-adlink;
			storagearea=storagearea+adlink-aulink;
			storearea.add(storagearea);
		}
		i1--;
		i4++;
		while(true) {
			if(i1>0) {
				storagearea=storagearea-aulink;
				if(storagearea<0) {
					storagearea=0;
				}
				storearea.add(storagearea);
				if(noofcams*(varculink1[i4]+varculink1[i4+1])>adlink) {
				extstr=extstr+noofcams*(varculink1[i4]+varculink1[i4+1])-adlink;
				storagearea=storagearea+adlink-aulink;
				}
				else {
					storagearea=storagearea+noofcams*(varculink1[i4]+varculink1[i4+1])-aulink;
					if(storagearea<0) {
						storagearea=0;
					}
				}
			    storearea.add(storagearea);
			    i1=i1-2;
			    i4=i4+2;
			}
			else {
				break;
			}
		}
		while(true) {
			if(extstr>=0) {
				storagearea=storagearea-aulink;
				storearea.add(storagearea);
				extstr=extstr-adlink;
				System.out.println(extstr);
				if(extstr<0) {
					storagearea=storagearea+extstr-aulink;
				}
				else {
				storagearea=storagearea+adlink-aulink;
				}
			    storearea.add(storagearea);
			    //System.out.println(storagearea+"fffeeew");
			}
			else {
				break;
			}
		}
		while(true) {
		    storagearea=storagearea-aulink;
		    if(storagearea<=0) {
		    	break;
				}
		    storearea.add(storagearea);
		    //System.out.println(storagearea+"    fff");
		}
		System.out.println(storecam);
		System.out.println(storearea);
		System.out.println(varculink);
		MatlabEngine mat2=MatlabEngine.startMatlab();
		MatlabEngine mat3=MatlabEngine.startMatlab();
		MatlabEngine mat4=MatlabEngine.startMatlab();
		char a='-';
		double[] a1=new double[storecam.size()];
		double[] a2=new double[storearea.size()];
		
		int[] y1=new int[storecam.size()];
		int[] y2=new int[storearea.size()];
		int k=0;
		for(double a3:storecam) {
			a1[k]=a3/8;
			y1[k]=k;
			k++;
		}
		k=0;
		for(double a3:storearea) {
			a2[k]=a3/8;
			y2[k]=k;
			k++;
		}
		Object handle=mat2.feval("plot",y1,a1,a);
		Object handle1=mat3.feval("plot",y2,a2,a);
		Object handle2=mat4.feval("plot",y3,varculink1,a);
		String s="Data stored in camera node";
		String s1="Time in ms";
		String s2="Data stored in area node";
		String s3="Time in ms";
		String s4="Data storage(in Bytes)";
		String s5="Data storage(in Bytes)";
		String s6="Band-width";
		String s7="Time in ms";
		String s8="Bandwidth variations of camera uplink";
		mat2.feval("xlabel",s1);
		mat2.feval("ylabel",s);
		mat2.feval("title",s4);
		mat3.feval("xlabel",s3);
		mat3.feval("ylabel",s2);
		mat3.feval("title",s5);
		mat4.feval("xlabel",s7);
		mat4.feval("ylabel",s6);
		mat4.feval("title",s8);
		while((boolean)mat2.feval("ishandle",handle)) {
            Thread.sleep(5000);
            }
		while((boolean)mat3.feval("ishandle",handle1)) {
			Thread.sleep(5000);
			}
		while((boolean)mat4.feval("ishandle",handle2)) {
			Thread.sleep(5000);
			}*/
		while(true) {
			if(i>0) {
				storagecam=storagecam+cdlink-culink;
			    storecam.add(storagecam);
			    storagecam=storagecam-culink;
			    storecam.add(storagecam);
			    i=i-2;
			}
			else {
			    storagecam=storagecam-culink;
			    if(storagecam<=0) {
			    	break;
			    }
			    storecam.add(storagecam);
			}
		}
		int i1=storecam.size();
		int extstr=0;
		if(adlink>noofcams*culink) {
			storagearea=storagearea+noofcams*culink-aulink;
			storearea.add(storagearea);
		}
		else {
			extstr=extstr+noofcams*culink-adlink;
			storagearea=storagearea+adlink-aulink;
			storearea.add(storagearea);
		}
		i1--;
		while(true) {
			if(i1>0) {
				storagearea=storagearea-aulink;
				storearea.add(storagearea);
				extstr=extstr+2*noofcams*culink-adlink;
				System.out.println(extstr);
				storagearea=storagearea+adlink-aulink;
			    storearea.add(storagearea);
			    i1=i1-2;
			}
			else {
				break;
			}
		}
		while(true) {
			if(extstr>=0) {
				storagearea=storagearea-aulink;
				storearea.add(storagearea);
				extstr=extstr-adlink;
				System.out.println(extstr);
				if(extstr<0) {
					storagearea=storagearea+extstr-aulink;
				}
				else {
				storagearea=storagearea+adlink-aulink;
				}
			    storearea.add(storagearea);
			}
			else {
				break;
			}
		}
		while(true) {
		    storagearea=storagearea-aulink;
		    if(storagearea<=0) {
		    	break;
		    }
		    storearea.add(storagearea);
		}
		System.out.println(storecam);
		System.out.println(storearea);
		MatlabEngine mat2=MatlabEngine.startMatlab();
		MatlabEngine mat3=MatlabEngine.startMatlab();
		char a='-';
		double[] a1=new double[storecam.size()];
		double[] a2=new double[storearea.size()];
		int[] y1=new int[storecam.size()];
		int[] y2=new int[storearea.size()];
		int k=0;
		for(double a3:storecam) {
			a1[k]=a3/8;
			System.out.println(a1[k]);
			y1[k]=k;
			k++;
		}
		k=0;
		for(double a3:storearea) {
			a2[k]=a3/8;
			y2[k]=k;
			k++;
		}
		Object handle=mat2.feval("plot",y1,a1,a);
		Object handle1=mat3.feval("plot",y2,a2,a);
		String s="Data stored in camera node";
		String s1="Time in ms";
		String s2="Data stored in area node";
		String s3="Time in ms";
		String s4="Data storage(in Bytes)";
		String s5="Data storage(in Bytes)";
		mat2.feval("xlabel",s1);
		mat2.feval("ylabel",s);
		mat2.feval("title",s4);
		mat3.feval("xlabel",s3);
		mat3.feval("ylabel",s2);
		mat3.feval("title",s5);
		while((boolean)mat2.feval("ishandle",handle)) {
            Thread.sleep(5000);
            }
		while((boolean)mat3.feval("ishandle",handle1)) {
			Thread.sleep(5000);
			}
		//System.out.println(areauplink+"      saketh"+ millis);
	}

	private void plotgraph() throws IllegalArgumentException, IllegalStateException, InterruptedException, MatlabExecutionException, MatlabSyntaxException, ExecutionException {
		mat=MatlabEngine.startMatlab();
		mat1=MatlabEngine.startMatlab();
		char a='-';
		double[] a1=new double[energy1.size()];
		int k=0;
		for(double i:energy1) {
			a1[k]=i;
			k++;
		}
		int[] y= new int[energy1.size()];
		for(int i=0;i<energy1.size();i++) {
			y[i]=i;
		}
		double[] a2=new double [nodes];
		int[] y1=new int [nodes];
		for(int i=0;i<nodes;i++) {
			a2[i]=allenergies[i];
			y1[i]=i;
		}
		Object handle=mat.feval("plot",y,a1,a);
		Object handle1=mat1.feval("bar",y1,a2);
		String s="Time instants";
		String s1="Energy at each time instant(in mJ)";
		String s2="Energy distribution of d-0 node";
		mat.feval("xlabel", s);
		mat.feval("ylabel", s1);
		mat.feval("title",s2);
		String s3="id of each node";
		String s4="Energy consumed(in mJ)";
		String s5="Energy consumed by all nodes";
		mat1.feval("xlabel", s3);
		mat1.feval("ylabel", s4);
		mat1.feval("title",s5);
		//Object handle=mat.feval("scatter",y,a3);
		while((boolean)mat.feval("ishandle",handle)) {
		              Thread.sleep(5000);
		}
		while((boolean)mat1.feval("ishandle",handle1)) {
            Thread.sleep(5000);
}
	}

	private void printNetworkUsageDetails() {
		System.out.println("Total network usage = "+NetworkUsageMonitor.getNetworkUsage()/Config.MAX_SIMULATION_TIME);		
	}

	private FogDevice getCloud(){
		for(FogDevice dev : getFogDevices())
			if(dev.getName().equals("cloud"))
				return dev;	
		return null;
	}
	
	private void printCostDetails(){
		System.out.println("Cost of execution in cloud = "+getCloud().getTotalCost());
	}
	
	private void printPowerDetails() {
		for(FogDevice fogDevice : getFogDevices()){
			System.out.println(fogDevice.getName() + " : Energy Consumed = "+fogDevice.getEnergyConsumption());

		}
	}

	private String getStringForLoopId(int loopId){
		for(String appId : getApplications().keySet()){
			Application app = getApplications().get(appId);
			for(AppLoop loop : app.getLoops()){
				if(loop.getLoopId() == loopId)
					return loop.getModules().toString();
			}
		}
		return null;
	}
	private void printTimeDetails() {
		energy1=FogDevice.getenergy();
		allenergies=FogDevice.getenergies();
		nodes=FogDevice.getnodes();
		System.out.println(energy1.size());
		System.out.println(allenergies.length);
		System.out.println(nodes);
		System.out.println("=========================================");
		System.out.println("============== RESULTS ==================");
		System.out.println("=========================================");
		System.out.println("EXECUTION TIME : "+ (Calendar.getInstance().getTimeInMillis() - TimeKeeper.getInstance().getSimulationStartTime()));
		System.out.println("=========================================");
		System.out.println("APPLICATION LOOP DELAYS");
		System.out.println("=========================================");
		System.out.println(TimeKeeper.getInstance().getLoopIdToCurrentAverage());//hashmap
		for(Integer loopId : TimeKeeper.getInstance().getLoopIdToTupleIds().keySet()){
			/*double average = 0, count = 0;
			for(int tupleId : TimeKeeper.getInstance().getLoopIdToTupleIds().get(loopId)){
				Double startTime = 	TimeKeeper.getInstance().getEmitTimes().get(tupleId);
				Double endTime = 	TimeKeeper.getInstance().getEndTimes().get(tupleId);
				if(startTime == null || endTime == null)
					break;
				average += endTime-startTime;
				count += 1;
			}
			System.out.println(getStringForLoopId(loopId) + " ---> "+(average/count));*/
			System.out.println(getStringForLoopId(loopId) + " ---> "+TimeKeeper.getInstance().getLoopIdToCurrentAverage().get(loopId));
		}
		System.out.println("=========================================");
		System.out.println("TUPLE CPU EXECUTION DELAY");
		System.out.println("=========================================");
		
		for(String tupleType : TimeKeeper.getInstance().getTupleTypeToAverageCpuTime().keySet()){
			System.out.println(tupleType + " ---> "+TimeKeeper.getInstance().getTupleTypeToAverageCpuTime().get(tupleType));
		}
		
		System.out.println("=========================================");
	}

	protected void manageResources(){
		send(getId(), Config.RESOURCE_MANAGE_INTERVAL, FogEvents.CONTROLLER_RESOURCE_MANAGE);
	}
	
	private void processTupleFinished(SimEvent ev) {
	}
	
	@Override
	public void shutdownEntity() {	
	}
	
	public void submitApplication(Application application, int delay, ModulePlacement modulePlacement){
		FogUtils.appIdToGeoCoverageMap.put(application.getAppId(), application.getGeoCoverage());
		getApplications().put(application.getAppId(), application);
		getAppLaunchDelays().put(application.getAppId(), delay);
		getAppModulePlacementPolicy().put(application.getAppId(), modulePlacement);
		
		for(Sensor sensor : sensors){
			sensor.setApp(getApplications().get(sensor.getAppId()));
		}
		for(Actuator ac : actuators){
			ac.setApp(getApplications().get(ac.getAppId()));
		}
		
		for(AppEdge edge : application.getEdges()){
			if(edge.getEdgeType() == AppEdge.ACTUATOR){
				String moduleName = edge.getSource();
				for(Actuator actuator : getActuators()){
					if(actuator.getActuatorType().equalsIgnoreCase(edge.getDestination()))
						application.getModuleByName(moduleName).subscribeActuator(actuator.getId(), edge.getTupleType());
				}
			}
		}	
	}
	
	public void submitApplication(Application application, ModulePlacement modulePlacement){
		submitApplication(application, 0, modulePlacement);
	}
	
	
	private void processAppSubmit(SimEvent ev){
		Application app = (Application) ev.getData();
		processAppSubmit(app);
	}
	
	private void processAppSubmit(Application application){
		System.out.println(CloudSim.clock()+" Submitted application "+ application.getAppId());
		FogUtils.appIdToGeoCoverageMap.put(application.getAppId(), application.getGeoCoverage());
		getApplications().put(application.getAppId(), application);
		
		ModulePlacement modulePlacement = getAppModulePlacementPolicy().get(application.getAppId());
		for(FogDevice fogDevice : fogDevices){
			sendNow(fogDevice.getId(), FogEvents.ACTIVE_APP_UPDATE, application);
		}
		
		Map<Integer, List<AppModule>> deviceToModuleMap = modulePlacement.getDeviceToModuleMap();
		for(Integer deviceId : deviceToModuleMap.keySet()){
			for(AppModule module : deviceToModuleMap.get(deviceId)){
				sendNow(deviceId, FogEvents.APP_SUBMIT, application);
				sendNow(deviceId, FogEvents.LAUNCH_MODULE, module);
			}
		}
	}

	public List<FogDevice> getFogDevices() {
		return fogDevices;
	}

	public void setFogDevices(List<FogDevice> fogDevices) {
		this.fogDevices = fogDevices;
	}

	public Map<String, Integer> getAppLaunchDelays() {
		return appLaunchDelays;
	}

	public void setAppLaunchDelays(Map<String, Integer> appLaunchDelays) {
		this.appLaunchDelays = appLaunchDelays;
	}

	public Map<String, Application> getApplications() {
		return applications;
	}

	public void setApplications(Map<String, Application> applications) {
		this.applications = applications;
	}

	public List<Sensor> getSensors() {
		return sensors;
	}

	public void setSensors(List<Sensor> sensors) {
		for(Sensor sensor : sensors)
			sensor.setControllerId(getId());
		this.sensors = sensors;
	}

	public List<Actuator> getActuators() {
		return actuators;
	}

	public void setActuators(List<Actuator> actuators) {
		this.actuators = actuators;
	}

	public Map<String, ModulePlacement> getAppModulePlacementPolicy() {
		return appModulePlacementPolicy;
	}

	public void setAppModulePlacementPolicy(Map<String, ModulePlacement> appModulePlacementPolicy) {
		this.appModulePlacementPolicy = appModulePlacementPolicy;
	}
}