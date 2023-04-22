public class DecimalConverter {

    static int binaryToUnsigned(String binaryString){

        int decimal = 0;
        int coef;

        for(int i = 0; i<binaryString.length(); i++){
            int each = 0 ;
            char currentChar = binaryString.charAt(i);
            try{

                if(currentChar == 48 || currentChar == 49){
                    each = currentChar - 48;
                }else{
                    throw new Exception("input is wrong");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            coef = binaryString.length() -1 -i ;
            decimal += Math.pow(2,coef) * each;
        }
        return decimal;
    }

    static int binaryToSigned(String binaryString){
        int decimal = 0;
        int coef;

        for(int i = 0;i<binaryString.length();i++){
            int each =0 ;
            char currentChar = binaryString.charAt(i);

            try{

                if(currentChar == 48 || currentChar == 49){
                    each = currentChar - 48;
                }else{
                    throw new Exception("input is wrong");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            coef = binaryString.length() -1 -i ;

            if(i==0){
                decimal -= Math.pow(2,coef) * each;
                continue;
            }
            decimal += Math.pow(2,coef) * each;
        }
        return decimal;
    }


    static String formatDouble(double value) {
        long bits = Double.doubleToRawLongBits(value);
        if (bits == 0x8000000000000000L)
            return "-0";
        else if (bits == 0x0000000000000000L)
            return "0";
        else if (String.valueOf(value).toLowerCase().contains("e"))
            return String.format("%.5e", value);
        else
            return String.format("%.5f", value);
    }

    public static String convertToFloat(String binary, int numOfBytes){
        try {
            return formatDouble(binaryTo1ByteFloatingPoint(binary, numOfBytes));
        }catch (InfinityException e){
            return ((binary.charAt(0)=='0' ? "+" : "-")  + "infinity" );
        }catch (NaNException e){
            return "NaN";
        }catch (Exception e){
            e.printStackTrace();
            return "Error";
        }
    }

    static double binaryTo1ByteFloatingPoint(String binaryString,int numOfBytes) throws InfinityException, NaNException {
        double value = 0;
        int numOfExpBits = 9999;

        if(numOfBytes == 1){
            numOfExpBits = 4;
        }else if(numOfBytes == 2){
            numOfExpBits = 6;
        }else if(numOfBytes == 3){
            numOfExpBits = 8;
        }else if(numOfBytes == 4){
            numOfExpBits = 10;
        }

        //number of exponent bit is used
        final int BIAS = (int) Math.pow(2,numOfExpBits-1)-1;

        char sign = binaryString.charAt(0);
        String exponentField = binaryString.substring(1,numOfExpBits+1);
        String fractionField = binaryString.substring(numOfExpBits+1,numOfBytes*8);


        //denormalized
        if(!exponentField.contains("1")){


            int E = 1 -BIAS;

            String strM = roundToNearestEven(fractionField,'0');


            String[] arr= strM.split("\\.");
            double M =0;
            double value1 = binaryToUnsigned(arr[0]);
            double value2 =0;

            for(int i = 0,k=-1;i<arr[1].length();i++,k--){
                double coef = Math.pow(2,k);
                value2 += coef*(arr[1].charAt(i)-48);
            }
            M = value1+value2;
            value=Math.pow(-1,sign-48)*M*Math.pow(2,E);
        }


        //special
        else if(!exponentField.contains("0")){

            if(fractionField.length()>13){

                String fr = fractionField.substring(0,13);

                //infinity
                if(!fr.contains("1")){// If first 13 characters 0


                    if(fractionField.charAt(13)=='1'){

                        //If  14. 1 se ve greater than half way ise 00000...1 NaN
                        if(fractionField.substring(14).contains("1")){
                            throw new NaNException("NaN");
                        }
                        //14. 1 se ve half way ise  0000...000 infinity
                        else{
                            throw new InfinityException("Infinity");
                        }
                    }//if 000000..00 infinity
                    else{
                        throw new InfinityException("Infinity");
                    }

                }
                //NaN //if 0-12 is 0
                else{
                    throw new NaNException("NaN");
                }



            }else{// if no need rounding
                //if all zero and not infinity, then NaN

                if(fractionField.contains("1")){
                    throw new NaNException("NaN");
                }else {
                    throw new InfinityException("Infinity");
                }


            }



        }//special case



        //normalized
        else{
            int E = binaryToUnsigned(exponentField) -BIAS;

            String strM = roundToNearestEven(fractionField,'1');

            //arr split edilmiyor HATAAAAAAAAAAAAAAAAA
            String[] arr= strM.split("\\.");
            double M =0;
            double value1 = binaryToUnsigned(arr[0]);
            double value2 =0;

            for(int i = 0,k=-1;i<arr[1].length();i++,k--){
                double coef = Math.pow(2,k);
                value2 += coef*(arr[1].charAt(i)-48);
            }
            M = value1+value2;
            value=Math.pow(-1,sign-48)*M*Math.pow(2,E);
        }

        return value;


    }

    static String add1(String fraction,char dec){
        String a = dec+".".concat(fraction.substring(0,13));
        char[] charArr = a.toCharArray();

        boolean cond = false;

        for(int i=charArr.length-1;i>=0;i--){
            if(charArr[i]=='0'){
                charArr[i]='1';
                cond=true;
                break;
            }else if(charArr[i]=='1'){
                charArr[i] = '0';
            }
        }
        a= String.valueOf(charArr);
        if(!cond){

            return "1".concat(a);
        }else{
            return a;
        }

    }

    static String roundToNearestEven(String fraction,char dec){

        if(fraction.length()>13){
            String extraPart = fraction.substring(13);
            //greater or halfway
            if(extraPart.charAt(0)=='1'){
                String rem = extraPart.substring(1);


                //Greater than half way add1
                if(rem.contains("1")){
                    return add1(fraction,dec);
                }
                //halfway round to nearest even
                else{
                    if(fraction.charAt(12)=='0'){
                        return dec+".".concat(fraction.substring(0,13));
                    }
                    else {//use add1
                        return add1(fraction,dec);
                    }

                }

            }
            //less than half way
            //else if(extraPart.charAt(0)==0){
            else{
                return dec+".".concat(fraction.substring(0,13));
            }

        }//if >13

        //<=13
        else{
            return dec+".".concat(fraction);
        }

    }



}
