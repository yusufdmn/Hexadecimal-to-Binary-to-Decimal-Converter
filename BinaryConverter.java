import java.util.*;

public class BinaryConverter {

    static Dictionary<Character,String> hexaToBinDictionary = new Hashtable<>();


    public static String[] generateBinaryList(String fullInput){
        defineHexaDictionary(hexaToBinDictionary);
        ArrayList<String> inputList = splitInput(fullInput);
        String[] nextDataToConvert = new String[InputData.dataSize];

        int binaryAmount = inputList.size() / InputData.dataSize;
        String[] binaryList = new String[binaryAmount];

        for(int i = 0; i < binaryAmount; i++){
            getNextData(inputList,nextDataToConvert);
            nextDataToConvert = setUpEndianType(nextDataToConvert, InputData.orderType);
            String binary = convertToBinary(nextDataToConvert);
            binaryList[i] = binary;
        }
        return binaryList;
    }

    static ArrayList<String> splitInput(String input){      // return arraylist that contains splitted (with " " and "\n") string

        ArrayList<String> inputList = new ArrayList<String>();
        String[] arrayOfInputLines = input.split("\n");

        for (String inputLine : arrayOfInputLines){
            String[] arrayOfLineInputs = inputLine.split(" ");
            ArrayList<String> inputOfLineList = new ArrayList<String>(Arrays.asList(arrayOfLineInputs));
            inputList.addAll(inputOfLineList);
        }
        return  inputList;
    }

    static void defineHexaDictionary(Dictionary<Character,String> hexaDictionary){
        hexaDictionary.put('0', "0000");
        hexaDictionary.put('1', "0001");
        hexaDictionary.put('2', "0010");
        hexaDictionary.put('3', "0011");
        hexaDictionary.put('4', "0100");
        hexaDictionary.put('5', "0101");
        hexaDictionary.put('6', "0110");
        hexaDictionary.put('7', "0111");
        hexaDictionary.put('8', "1000");
        hexaDictionary.put('9', "1001");
        hexaDictionary.put('a', "1010");
        hexaDictionary.put('b', "1011");
        hexaDictionary.put('c', "1100");
        hexaDictionary.put('d', "1101");
        hexaDictionary.put('e', "1110");
        hexaDictionary.put('f', "1111");
    }

    static void getNextData(ArrayList<String> inputList, String[] nextInputToConvert){
        for (int i  = 0; i < InputData.dataSize;i++){
            nextInputToConvert[i] = inputList.get(i);
        }
        inputList.subList(0, InputData.dataSize).clear();
    }

    static String convertToBinary(String[] hexaData){
        String binary = "";
        for(int i = 0; i < InputData.dataSize; i++){
            binary += hexaToBinDictionary.get(hexaData[i].charAt(0));
            binary += hexaToBinDictionary.get(hexaData[i].charAt(1));
        }
        return binary;
    }

    static String[] setUpEndianType(String[] endianArray, InputData.OrderType orderType){
        if (orderType == InputData.OrderType.LittleEndian){
            String[] litteEndianArray = new String[InputData.dataSize];
            for (int i = 0; i < InputData.dataSize; i++){
                litteEndianArray[InputData.dataSize-1-i] = endianArray[i];
            }
            return litteEndianArray;
        }
        else {
            return endianArray;
        }
    }

}