package com.eeg.datahandler;

import java.util.List;

public class ConvertData {

    // Amplifier Data Conversion
    final static float fs = 250.0f; // Sampling frequency (should be taken from user input)
    final static float amp_V_ref = 4.5f; // Reference voltage for ADC
    final static float amp_gain = 24; // PG of ADS1299
    final static float scale_fac_uVolts_per_count = (float) (amp_V_ref / (Math.pow(2,23)-1) / amp_gain * 1000000.f);


    // this method is passed a 3 byte array
    static int interpret24bitAsInt32(byte[] byteArray) {
        //little endian
        int newInt = ( ((0xFF & byteArray[0]) << 16) |
                ((0xFF & byteArray[1]) << 8) |
                (0xFF & byteArray[2]) );

        if((newInt & 0x00800000) > 0) {
            newInt |= 0xFF000000;
        } else {
            newInt &= 0x00FFFFFF;
        }
        return newInt;
    }


    public static float convertByteToMicroVolts(byte[] byteArray) {
        return scale_fac_uVolts_per_count * interpret24bitAsInt32(byteArray);
    }

    //Support method to convert float array as a comma separated string
    public static String convertFloatArrayToString(List<Float>[] input){
        StringBuilder sb = new StringBuilder();
        int len = input.length;

        for(int i=0;i<len;i++){
            sb.append(input[i]);
            sb.append(", ");
        }

        return sb.toString();
    }

}
