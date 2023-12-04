package org.fog.test.perfeval;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;


public class datatransmission_failednode {

	public static void main(String[] args) throws IllegalArgumentException, IllegalStateException, InterruptedException, RejectedExecutionException, ExecutionException {
		for(int w=0;w<1;w++) {
		int aulink=10000;
		aulink=aulink/1000;
		int adlink=30000;
		adlink=adlink/1000;
		int culink=4000;
		culink=culink/1000;
		int cdlink=10000;
		cdlink=cdlink/1000;
		int noofcams=9;
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
		MatlabEngine mat1=MatlabEngine.startMatlab();
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
				k11++;
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
			}
	}
	}

}



