import java.io.File;
import java.io.IOException;

public class TestOCR {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String path = "D:\\passCodeNewAction.jpg";   
        try {   
            String valCode = new OCR().recognizeText(new File(path), "png");   
            System.out.println(valCode);   
        } catch (IOException e) {   
            e.printStackTrace();   
        } catch (Exception e) {
			e.printStackTrace();
		}    
	}

}
