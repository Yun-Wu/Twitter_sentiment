import java.io.IOException;

import libsvm.*;

public class SVM {
	public static void main(String[] args) throws IOException {
//		String[] trainArgs = {"-v", "10", "/u/chenqr/cs388/hw4/feature/OtherFeature"};
		svm_train.main(args);
		 
//		String[] testArgs = {"/u/chenqr/cs388/hw4/test", modelFile, "/u/chenqr/cs388/hw4/svmresult"};
		
//		Double accuracy = svm_predict.main(testArgs);
//		System.out.println("SVM Classification is done. Accuracy is " + accuracy);
		System.out.println("SVM Classification is done.");
		
	}
	
	
}
