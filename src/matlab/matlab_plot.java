package matlab;
//Take some time to execute
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;

public class matlab_plot {
	private static MatlabEngine mat;
	public static void main(String[] args) throws IllegalArgumentException, IllegalStateException, InterruptedException, MatlabExecutionException, MatlabSyntaxException, ExecutionException {
		mat=MatlabEngine.startMatlab();
		//mat.eval("x=[-10:0.1:10]");
		//mat.eval("y=x.^2");
		//mat.eval("plot(x,y)");
		char a='-';
		//int[] x1= {1, 1, 0, 1, 2, 1, 0, 3, 4, 2, 2, 2, 1, 0, 0, 2, 1, 0, 3, 4, 4, 4, 0, 4};
		//int t1=x1.length/2;
		int[] x= {0,0,0,2,2,5,5,2,0,0,5,5,5,5,5,3,3,3};
		int[] y8=new int[x.length];
		int k11=0;
		for(int q:x) {
			y8[k11]=k11;
			k11++;
		}
		String s21="id of each node";
		String s22="Number of camera working";
		String s23="Nodes status";
		int s2=17;
		String q="k";
		MatlabEngine mat1=MatlabEngine.startMatlab();
		Object handle1=mat1.feval("bar",y8,x);
		mat1.feval("xlabel",s21,"FontWeight","bold","FontSize",s2);
		mat1.feval("ylabel",s22,"FontWeight","bold","FontSize",s2);
		mat1.feval("title",s23,"FontWeight","bold","FontSize",s2); 
		while((boolean)mat1.feval("ishandle",handle1)) {
			Thread.sleep(5000);
			}
	}
}

/*package matlab;
//Take some time to execute
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;

public class matlab_plot {
	private static MatlabEngine mat;
	public static void main(String[] args) throws IllegalArgumentException, IllegalStateException, InterruptedException, MatlabExecutionException, MatlabSyntaxException, ExecutionException {
		mat=MatlabEngine.startMatlab();
		//mat.eval("x=[-10:0.1:10]");
		//mat.eval("y=x.^2");
		//mat.eval("plot(x,y)");
		char a='-';
		int[] x1= {1, 1, 0, 1, 2, 1, 0, 3, 4, 2, 2, 2, 1, 0, 0, 2, 1, 0, 3, 4, 4, 4, 0, 4};
		int t1=x1.length/2;
		int[] x= {0, 7, 2, 2, 0, 7, 9, 8, 6, 10, 10, 8, 10, 5, 10, 1, 9, 10, 6, 1, 0, 0, 3, 8, 2, 7, 2, 0, 5, 0, 7, 8, 1, 3, 5, 6, 3, 10, 10, 10, 9, 4, 9, 8, 1, 0, 5, 2, 3, 10, 6, 1, 3, 0, 10, 7, 2, 10};	
		int a1=x.length;
		int[] uplinkarea2=new int[a1+t1];
		int[] y8=new int[a1+t1];
		int k11=t1;
		k11=t1;
		for(int a3:x) {
				uplinkarea2[k11]=a3;
				k11++;
		}
		for(int k12=0;k12<a1+t1;k12++) {
			y8[k12]=k12;
		}
		String s21="Time in ms";
		String s22="Data stored in extranode";
		String s23="Data Storage(in Bytes";
		int s2=17;
		String q="k";
		MatlabEngine mat1=MatlabEngine.startMatlab();
		Object handle1=mat1.feval("plot",y8,uplinkarea2,"Color",q,"LineWidth",2);
		mat1.feval("xlabel",s21,"FontWeight","bold","FontSize",s2);
		mat1.feval("ylabel",s22,"FontWeight","bold","FontSize",s2);
		mat1.feval("title",s23,"FontWeight","bold","FontSize",s2); 
		while((boolean)mat1.feval("ishandle",handle1)) {
			Thread.sleep(5000);
			}
	}
}*/
