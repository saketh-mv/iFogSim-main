package org.fog.test.perfeval;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;


public class datatransmission {

	public static void main(String[] args) throws IllegalArgumentException, IllegalStateException, InterruptedException, RejectedExecutionException, ExecutionException {
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
		String q="k";
		int s21=17;
		Object handle1=mat1.feval("plot",y1,camera,a,"Color",q,"LineWidth",2);
		Object handle2=mat2.feval("plot",y2,area,a,"Color",q,"LineWidth",2);
		Object handle3=mat3.feval("plot",y3,varculink1,a,"Color",q,"LineWidth",2);
		Object handle4=mat4.feval("plot",y4,downlinkarea,a,"Color",q,"LineWidth",2);
		Object handle5=mat5.feval("plot",y5,uplinkarea,a,"Color",q,"LineWidth",2);
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
		mat1.feval("xlabel",s1,"FontWeight","bold","FontSize",s21);
		mat1.feval("ylabel",s,"FontWeight","bold","FontSize",s21);
		mat1.feval("title",s2,"FontWeight","bold","FontSize",s21);
		mat2.feval("xlabel",s4,"FontWeight","bold","FontSize",s21);
		mat2.feval("ylabel",s3,"FontWeight","bold","FontSize",s21);
		mat2.feval("title",s5,"FontWeight","bold","FontSize",s21);
		mat3.feval("xlabel",s7,"FontWeight","bold","FontSize",s21);
		mat3.feval("ylabel",s6,"FontWeight","bold","FontSize",s21);
		mat3.feval("title",s8,"FontWeight","bold","FontSize",s21);
		mat4.feval("xlabel",s10,"FontWeight","bold","FontSize",s21);
		mat4.feval("ylabel",s9,"FontWeight","bold","FontSize",s21);
		mat4.feval("title",s11,"FontWeight","bold","FontSize",s21);
		mat5.feval("xlabel",s13,"FontWeight","bold","FontSize",s21);
		mat5.feval("ylabel",s12,"FontWeight","bold","FontSize",s21);
		mat5.feval("title",s14,"FontWeight","bold","FontSize",s21);
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
		/*while(true) {
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
		int extstr1=0;
		while(true) {
			if(extstr>=0) {
				storagearea=storagearea-aulink;
				storearea.add(storagearea);
				extstr1=extstr;
				extstr=extstr-adlink;
				System.out.println(extstr);
				if(extstr<0) {
					storagearea=storagearea+extstr1-aulink;
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
		System.out.println(storecam+"----->camera storage");
		System.out.println(storearea+"------>area storage");
		System.out.println(varculink+"------>uplink variations");*/
		/*while(true) {
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
		int extstr1=0;
		while(true) {
			if(extstr>=0) {
				storagearea=storagearea-aulink;
				storearea.add(storagearea);
				extstr1=extstr;
				extstr=extstr-adlink;
				System.out.println(extstr);
				if(extstr<0) {
					System.out.println("   "+storagearea);
					storagearea=storagearea+extstr1-aulink;
					System.out.println("   "+storagearea+" "+extstr1+"  "+aulink);
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
			}*/
	}

}
