import java.io.*;
import java.util.Map;

/**
  * User: gm
 * Date: 3/9/12
 */
public class CommonTestMethods {
    static public boolean arrayContainsItem(String[] array, String item2Find){
        for(String item : array){
            if (item.equals(item2Find)) return true;
        }
        return false;
    }

    static public void createFile(String tFileName) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(tFileName, true))) {
            writer.print("12345");
        } catch (IOException e){ throw e;}
    }

    static public String readFileAsString(String filePath) throws java.io.IOException{
        byte[] buffer = new byte[(int) new File(filePath).length()];
        try(BufferedInputStream f = new BufferedInputStream(new FileInputStream(filePath))) {
            f.read(buffer);
        }
        return new String(buffer);
    }

    static public void createConfigFile(String tFileName) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(tFileName, true))) {
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.println("<root>");
            writer.println("    <interval seconds = \"*\"/>");
            writer.println("    <log level = \"LL\"/>");
            writer.println("    <SynchronisedPaths>");
            writer.println("        <path name = \"sh1\" value = \"/synch1/\"/>");
            writer.println("        <path name = \"sh1_\" value = \"/synch2/\"/>");

            writer.println("        <path name = \"sh2\" value = \"/synch3/\"/>");
            writer.println("        <path name = \"sh2_\" value = \"/synch4/\"/>");
            writer.println("    </SynchronisedPaths>");
            writer.println("</root>");
        } catch (IOException e){ throw e;}
    }

    static public String GetValue(Map<String, String> map, String key) {
        for (Map.Entry<String, String> mapEntry : map.entrySet()) {
            if(mapEntry.getKey().equalsIgnoreCase(key)){
                return mapEntry.getValue();
            }
        }
        return null;
    }

    static public String getLine(String fileName, int lineNumber) throws Exception{
        int currentLineNumber = 0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(fileName))))) {
            String line;
            while ((line = br.readLine()) != null)   {
                if (currentLineNumber++ == lineNumber) return line;
                return line;
            }

        };
        throw new Exception("Could not find the line " + lineNumber);
    }
}
