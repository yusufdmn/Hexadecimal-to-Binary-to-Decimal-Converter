import javax.xml.crypto.Data;
import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

public class InputData {
    public enum DataType{
        SignedInteger,
        UnsignedInteger,
        FloatingPointNumber
    }
    public enum OrderType{
        LittleEndian,
        BigEndian
    }

    public static DataType dataType = DataType.FloatingPointNumber;
    public static OrderType orderType = OrderType.LittleEndian;

    public static String input;
    static int dataSize = 4;

    static String inputFileName;


    static String getFileContent(String fileName){
        String code = "";
        try {
            FileReader fileReader = new FileReader("./"+fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            bufferedReader.close();
            code = sb.toString();
        } catch (IOException e) {
            System.out.println("Could not find the spesific file." + e.getMessage());
        }
        return code;
    }

    static void getInput(){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Input file name: ");
        inputFileName = scanner.nextLine();
        input = getFileContent(inputFileName);

        System.out.print("Byte ordering: ");
        orderType = (scanner.nextLine().equals("l"))? OrderType.LittleEndian: OrderType.BigEndian;

        System.out.print("Data type: ");
        String dataTypeInput = scanner.nextLine();
        if(dataTypeInput.equals(("int")))
            dataType = DataType.SignedInteger;
        else if(dataTypeInput.equals(("unsigned")))
            dataType = DataType.UnsignedInteger;
        else if(dataTypeInput.equals(("float")))
            dataType = DataType.FloatingPointNumber;
        else{
            System.out.println("ERROR: You Entered Invalid DataType");
        }

        System.out.print("Data type Size: ");
        dataSize = scanner.nextInt();
    }
}