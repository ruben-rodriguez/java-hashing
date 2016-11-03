
import java.awt.Component;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Akiyoshi
 */
public class hash {

    private static StringBuilder sb = new StringBuilder();

    /**
     * Cadenas aleatorias iniciales
     */
    private static String h0 = "01100111010001010010001100000001";
    private static String h1 = "11101111110011011010101110001001";
    private static String h2 = "10011000101110101101110011111110";
    private static String h3 = "00010000001100100101010001110110";
    private static String h4 = "11000011110100101110000111110000";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String A = h0;
        String B = h1;
        String C = h2;
        String D = h3;
        String E = h4;

        System.out.print("Introduzca texto a cifrar: ");
        Scanner input = new Scanner(System.in);
        String test = input.nextLine();

        while (test.length() > 10 || test.length() == 0) {
            JOptionPane.showMessageDialog(null, "Longitud de cadena no valida. Introduzca otra.", "Atencion: ", JOptionPane.INFORMATION_MESSAGE);
            System.out.print("Introduzca texto a cifrar: ");
            test = input.nextLine();
        }

        input.close();

        System.out.println("Cadena a convertir a binario: " + test);
        sb.append("Cadena a convertir a binario: " + test + System.getProperty("line.separator"));

        String bin = AsciiToBinary(test);

        int longitudCadena = bin.length();
        String longitudBin = NumtoBin(longitudCadena);
        String longitudBin64 = Binto64(longitudBin);
        bin += "1";
        System.out.println("Juntamos y ponemos un 1 al final de la cadena: " + bin);
        sb.append("Juntamos y ponemos un 1 al final de la cadena: " + bin + System.getProperty("line.separator"));

        System.out.println("");
        sb.append(System.getProperty("line.separator"));
        System.out.println("Longitud de la cadena " + longitudCadena + " cuya representacion binaria es " + longitudBin);
        sb.append("Longitud de la cadena " + longitudCadena + " cuya representacion binaria es " + longitudBin + System.getProperty("line.separator"));
        System.out.println("Valor de la longitud en 64 bits: " + longitudBin64);
        sb.append("Valor de la longitud en 64 bits: " + longitudBin64 + System.getProperty("line.separator"));

        //Concatenamos un 1 al final de la cadena
        while (bin.length() % 512 != 448) {
            bin += "0";
        }

        System.out.println("Ponemos 0's hasta que la longitud de la cadena sea congruente con 448 modulo 512");
        sb.append("Ponemos 0's hasta que la longitud de la cadena sea congruente con 448 modulo 512" + System.getProperty("line.separator"));
        System.out.println(bin);
        sb.append(bin + System.getProperty("line.separator"));
        System.out.println("Longitud de la cadena: " + bin.length() + " congruente con 448 modulo 512");
        sb.append("Longitud de la cadena: " + bin.length() + " congruente con 448 modulo 512" + System.getProperty("line.separator"));
        System.out.println();
        sb.append(System.getProperty("line.separator"));

        String binLongitud = bin.concat(longitudBin64);
        System.out.println("Concatenamos la longitud en 64 bits del mensaje original al final de la cadena: " + binLongitud);
        sb.append("Concatenamos la longitud en 64 bits del mensaje original al final de la cadena: " + binLongitud + System.getProperty("line.separator"));

        if (binLongitud.length() % 512 == 0) {
            System.out.println("La cadena resultante es multiplo de 512, podemos continuar...");
            sb.append("La cadena resultante es multiplo de 512, podemos continuar..." + System.getProperty("line.separator"));
        } else {
            System.out.println("Se ha producido algun error... (resultado no es multipo de 512)");
            sb.append("Se ha producido algun error... (resultado no es multipo de 512)" + System.getProperty("line.separator"));
            System.exit(0);
        }

        System.out.println("");
        sb.append(System.getProperty("line.separator"));
        System.out.println("Partimos la cadena en chunks de 32 bits:");
        sb.append("Partimos la cadena en chunks de 32 bits:" + System.getProperty("line.separator"));

        ArrayList<String> words = toWords(binLongitud);
        for (int i = 0; i < words.size(); i++) {
            System.out.println("Chunk [" + (i + 1) + "]:" + words.get(i));
            sb.append("Chunk [" + (i + 1) + "]:" + words.get(i) + System.getProperty("line.separator"));
        }
        System.out.println("");
        sb.append(System.getProperty("line.separator"));

        System.out.println("Extendemos esas palabras (16 en total) a 80, aplicando xor y siguiendo el algoritmo:");
        sb.append("Extendemos esas palabras (16 en total) a 80, aplicando xor y siguiendo el algoritmo:" + System.getProperty("line.separator"));

        System.out.println("Desde i = 16 a i = 79, (i - 3) XOR (i - 8) XOR (i - 14) XOR (i - 16");
        sb.append("Desde i = 16 a i = 79, (i - 3) XOR (i - 8) XOR (i - 14) XOR (i - 16" + System.getProperty("line.separator"));
        ArrayList<String> extendedWords = extend(words);

        System.out.println("Todas las palabras generadas:");
        sb.append("Todas las palabras generadas:" + System.getProperty("line.separator"));
        for (int i = 0; i < extendedWords.size(); i++) {
            System.out.println("Chunk [" + (i) + "]:" + extendedWords.get(i));
            sb.append("Chunk [" + (i) + "]:" + extendedWords.get(i) + System.getProperty("line.separator"));
        }

        System.out.println("");
        sb.append(System.getProperty("line.separator"));
        System.out.println("Dependiendo del indice de la palabra, esta se envia una funcion y se realizan calculos logicos con las variables A=h0 B =h1 C=h2 D=h3 E=h4 y una k determinada.");
        sb.append("Dependiendo del indice de la palabra, esta se envia una funcion y se realizan calculos logicos con las variables A=h0 B =h1 C=h2 D=h3 E=h4 y una k determinada." + System.getProperty("line.separator"));
        System.out.println("");
        sb.append(System.getProperty("line.separator"));

        for (int i = 0; i < extendedWords.size(); i++) {

            System.out.println("------------------------------------------------");
            System.out.println("Palabra[" + i + "]");
            sb.append("------------------------------------------------" + System.getProperty("line.separator"));
            sb.append("Palabra[" + i + "]" + System.getProperty("line.separator"));

            String K = "";
            String F = "";

            if (i >= 0 && i <= 19) {
                String notB = "";
                for (int j = 0; j < B.length(); j++) {
                    if (B.charAt(j) == '1') {
                        notB += "0";
                    } else {
                        notB += "1";
                    }
                }

                String notBandD = checkChunk(NumtoBin(binaryToNum(notB) & binaryToNum(D)));
                String BandC = checkChunk(NumtoBin(binaryToNum(B) & binaryToNum(C)));

                F = checkChunk(NumtoBin(binaryToNum(notBandD) | binaryToNum(BandC)));
                K = "01011010100000100111100110011001";

                System.out.println("");
                System.out.println("FUNCION 1: ");
                System.out.println("!B: " + notB);
                System.out.println("!B AND D: " + notBandD);
                System.out.println(" B AND C: " + BandC);
                System.out.println("");
                System.out.println(" F ( (!B AND C) OR (B AND C)): " + F);
                System.out.println(" K: " + K);
                System.out.println("");
                sb.append(System.getProperty("line.separator"));
                sb.append("FUNCION 1: " + System.getProperty("line.separator"));
                sb.append("!B: " + notB + System.getProperty("line.separator"));
                sb.append("!B AND D: " + notBandD + System.getProperty("line.separator"));
                sb.append(" B AND C: " + BandC + System.getProperty("line.separator"));
                sb.append(System.getProperty("line.separator"));
                sb.append(" F ( (!B AND C) OR (B AND C)): " + F + System.getProperty("line.separator"));
                sb.append(" K: " + K + System.getProperty("line.separator"));
                sb.append(System.getProperty("line.separator"));

            } else if (i >= 20 && i <= 39) {

                String BxorC = checkChunk(NumtoBin(binaryToNum(B) ^ binaryToNum(C)));

                F = checkChunk(NumtoBin(binaryToNum(BxorC) ^ binaryToNum(D)));
                K = "01101110110110011110101110100001";

                System.out.println("");
                System.out.println("FUNCION 2: ");
                System.out.println(" B XOR C: " + BxorC);
                System.out.println("");
                System.out.println(" F ( (B XOR C) XOR D ): " + F);
                System.out.println(" K: " + K);
                System.out.println("");
                sb.append(System.getProperty("line.separator"));
                sb.append("FUNCION 2: " + System.getProperty("line.separator"));
                sb.append(" B XOR C: " + BxorC + System.getProperty("line.separator"));
                sb.append(System.getProperty("line.separator"));
                sb.append(" F ( (B XOR C) XOR D ): " + F + System.getProperty("line.separator"));
                sb.append(" K: " + K + System.getProperty("line.separator"));
                sb.append(System.getProperty("line.separator"));

            } else if (i >= 40 && i <= 59) {

                String BandC = checkChunk(NumtoBin(binaryToNum(B) & binaryToNum(C)));
                String BandD = checkChunk(NumtoBin(binaryToNum(B) & binaryToNum(D)));
                String CandD = checkChunk(NumtoBin(binaryToNum(C) & binaryToNum(D)));
                String OR = checkChunk(NumtoBin(binaryToNum(BandC) | binaryToNum(BandD)));

                F = checkChunk(NumtoBin(binaryToNum(OR) | binaryToNum(CandD)));
                K = "10001111000110111011110011011100";

                System.out.println("");
                System.out.println("FUNCION 3: ");
                System.out.println(" B AND C: " + BandC);
                System.out.println(" B AND D: " + BandD);
                System.out.println(" C AND D: " + CandD);
                System.out.println("");
                System.out.println(" F ( ((B AND C) OR (B AND D)) OR (C AND D)): " + F);
                System.out.println(" K: " + K);
                System.out.println("");
                sb.append(System.getProperty("line.separator"));
                sb.append("FUNCION 3: " + System.getProperty("line.separator"));
                sb.append(" B AND C: " + BandC + System.getProperty("line.separator"));
                sb.append(" B AND D: " + BandD + System.getProperty("line.separator"));
                sb.append(" C AND D: " + CandD + System.getProperty("line.separator"));
                sb.append(System.getProperty("line.separator"));
                sb.append(" F ( ((B AND C) OR (B AND D)) OR (C AND D)): " + F + System.getProperty("line.separator"));
                sb.append(" K: " + K + System.getProperty("line.separator"));
                sb.append(System.getProperty("line.separator"));

            } else if (i >= 60 && i <= 79) {

                String BxorC = checkChunk(NumtoBin(binaryToNum(B) ^ binaryToNum(C)));

                F = checkChunk(NumtoBin(binaryToNum(BxorC) ^ binaryToNum(D)));
                K = "11001010011000101100000111010110";

                System.out.println("");
                System.out.println("FUNCION 4: ");
                System.out.println(" B XOR C: " + BxorC);
                System.out.println("");
                System.out.println(" F ( (B XOR C) XOR D ): " + F);
                System.out.println(" K: " + K);
                System.out.println("");
                sb.append(System.getProperty("line.separator"));
                sb.append("FUNCION 4: " + System.getProperty("line.separator"));
                sb.append(" B XOR C: " + BxorC + System.getProperty("line.separator"));
                sb.append(System.getProperty("line.separator"));
                sb.append(" F ( (B XOR C) XOR D ): " + F + System.getProperty("line.separator"));
                sb.append(" K: " + K + System.getProperty("line.separator"));
                sb.append(System.getProperty("line.separator"));
            }

            String leftA = A.substring(5, A.length());
            for (int z = 0; z < 5; z++) {
                leftA += A.charAt(z);
            }

            String temp = checkChunk(NumtoBin(binaryToNum(leftA) + binaryToNum(F)));
            if (temp.length() == leftA.length()) {
                temp = "1" + temp;
            }
            String temp1 = NumtoBin(binaryToNum(temp) + binaryToNum(E));
            if (temp1.length() == temp.length()) {
                temp1 = "1" + temp1;
            }
            String temp2 = NumtoBin(binaryToNum(temp1) + binaryToNum(K));
            if (temp2.length() == temp1.length()) {
                temp2 = "1" + temp2;
            }
            String temp3 = NumtoBin(binaryToNum(temp2) + binaryToNum(extendedWords.get(i)));
            if (temp3.length() == temp2.length()) {
                temp3 = "1" + temp3;
            }

            if (temp3.length() > 32) {
                int truncate = temp3.length() - 32;
                temp3 = temp3.substring(truncate, temp3.length());
            }

            E = D;
            D = C;
            String leftB = B.substring(30, B.length());
            for (int x = 0; x < 30; x++) {
                leftB += B.charAt(x);
            }
            C = leftB;
            B = A;
            A = temp3;

            System.out.println("");
            System.out.println("Tras hacer los cambios de los estados E = D, D = C, C = deslplazamiento de B,");
            System.out.println("B = A y A = temporal calculado: ");
            System.out.println("   A: " + temp3);
            System.out.println("   B: " + B);
            System.out.println("   C: " + C);
            System.out.println("   D: " + D);
            System.out.println("   E: " + E);
            System.out.println("");
            sb.append(System.getProperty("line.separator"));
            sb.append("Tras hacer los cambios de los estados E = D, D = C, C = deslplazamiento de B," + System.getProperty("line.separator"));
            sb.append("B = A y A = temporal calculado: " + System.getProperty("line.separator"));
            sb.append("   A: " + temp3 + System.getProperty("line.separator"));
            sb.append("   B: " + B + System.getProperty("line.separator"));
            sb.append("   C: " + C + System.getProperty("line.separator"));
            sb.append("   D: " + C + System.getProperty("line.separator"));
            sb.append("   E: " + E + System.getProperty("line.separator"));
            sb.append(System.getProperty("line.separator"));

            System.out.println("------------------------------------------------");
            sb.append("------------------------------------------------" + System.getProperty("line.separator"));

        }

        h0 = NumtoBin(binaryToNum(h0) + binaryToNum(A));
        if (h0.length() > 32) {
            int truncated = h0.length() - 32;
            h0 = h0.substring(truncated, h0.length());
        }

        h1 = NumtoBin(binaryToNum(h1) + binaryToNum(B));
        if (h1.length() > 32) {
            int truncated = h1.length() - 32;
            h1 = h1.substring(truncated, h1.length());
        }

        h2 = NumtoBin(binaryToNum(h2) + binaryToNum(C));
        if (h2.length() > 32) {
            int truncated = h2.length() - 32;
            h2 = h2.substring(truncated, h2.length());
        }

        h3 = NumtoBin(binaryToNum(h3) + binaryToNum(D));
        if (h3.length() > 32) {
            int truncated = h3.length() - 32;
            h3 = h3.substring(truncated, h3.length());
        }

        h4 = NumtoBin(binaryToNum(h4) + binaryToNum(E));
        if (h4.length() > 32) {
            int truncated = h4.length() - 32;
            h4 = h4.substring(truncated, h4.length());
        }

        String hex1 = Long.toHexString(Long.parseLong(h0, 2));
        String hex2 = Long.toHexString(Long.parseLong(h1, 2));
        String hex3 = Long.toHexString(Long.parseLong(h2, 2));
        String hex4 = Long.toHexString(Long.parseLong(h3, 2));
        String hex5 = Long.toHexString(Long.parseLong(h4, 2));
        
        
        System.out.println("");
        System.out.println("Varibales hi finales y su valor en hexadecimal: ");
        System.out.println("h0: " + h0 + "   " + hex1);
        System.out.println("h1: " + h1 + "   " + hex2);
        System.out.println("h2: " + h2 + "   " + hex3);
        System.out.println("h3: " + h3 + "   " + hex4);
        System.out.println("h4: " + h4 + "   " + hex5);

        String hexFinal = hex1 + hex2 + hex3 + hex4 + hex5;
        System.out.println("Resultado hashing: " + hexFinal);
        
        sb.append(System.getProperty("line.separator"));
        sb.append("Varibales hi finales y su valor en hexadecimal: " + System.getProperty("line.separator"));
        sb.append("h0: " + h0 + "   " + hex1 + System.getProperty("line.separator"));
        sb.append("h1: " + h1 + "   " + hex2 + System.getProperty("line.separator"));
        sb.append("h2: " + h2 + "   " + hex3 + System.getProperty("line.separator"));
        sb.append("h3: " + h3 + "   " + hex4 + System.getProperty("line.separator"));
        sb.append("h4: " + h4 + "   " + hex5 + System.getProperty("line.separator"));
        sb.append("Resultado hashing: " + hexFinal + System.getProperty("line.separator"));

        try {
            dumpFile();
            System.out.println("Archivo dump guardado con exito.");
            System.exit(0);
        } catch (IOException ex) {
            Logger.getLogger(hash.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static String Binto64(String bin) {
        String result = "";
        for (int i = bin.length(); i < 64; i++) {
            result += "0";
        }
        return result.concat(bin);
    }

    public static String AsciiToBinary(String asciiString) {

        byte[] bytes = asciiString.getBytes();
        StringBuilder binary = new StringBuilder();
        StringBuilder resultado = new StringBuilder();
        String values = "";

        for (int i = 0; i < bytes.length; i++) {
            values += (bytes[i]) + " ";
        }

        System.out.println("Valor en ascii de la cadena: " + values);
        sb.append("Valor en ascii de la cadena: " + values + System.getProperty("line.separator"));

        for (int j = 0; j < bytes.length; j++) {
            int val = bytes[j];
            for (int i = 0; i < 8; i++) {
                if ((val & 128) == 0) { //EVALUAR AND LOGICO DEL 
                    resultado.append(0);
                    binary.append(0);
                } else {
                    resultado.append(1);
                    binary.append(1);
                }
                val <<= 1;  //DESPLAZAMIENTO LOGICO A LA IZQUIERDA
            }
            binary.append(' ');

        }
        System.out.println("Valor binario de la cadena: " + binary);
        return resultado.toString();
    }

    private static ArrayList<String> toWords(String binLongitud) {

        int chunk = 0;
        int chunkSize = binLongitud.length() / 32;
        ArrayList<String> result = new ArrayList<>();

        for (int i = 0; i < chunkSize; i++) {
            result.add(binLongitud.substring(chunk, chunk + 32));
            chunk += 32;
        }
        return result;
    }

    private static ArrayList<String> extend(ArrayList<String> words) {

        for (int i = 16; i < 80; i++) {
            System.out.println("");
            System.out.println("-------------------------------------------------------------------------------------------------------------------");
            System.out.println("Chunk[" + i + "]:");
            System.out.println("");

            String a = words.get(i - 3);
            String b = words.get(i - 8);
            String c = words.get(i - 14);
            String d = words.get(i - 16);
            long numFinal = binaryToNum(a) ^ binaryToNum(b);
            long numFinalb = numFinal ^ binaryToNum(c);
            long numFinalc = numFinalb ^ binaryToNum(d);
            String value = NumtoBin(numFinalc);

            while (value.length() != 32) {
                String aux = "0";
                value = aux + value;
            }

            String toAdd = value.substring(1, value.length());
            toAdd += value.charAt(0);

            System.out.println("Chunk[" + (i - 3) + "]" + a + " XOR " + "Chunk[" + (i - 8) + "]" + b + "\nXOR " + "Chunk[" + (i - 14) + "]" + c + " XOR " + "Chunk[" + (i - 16) + "]" + d);
            System.out.println("");
            System.out.println("Resultado: " + toAdd);
            System.out.println("-------------------------------------------------------------------------------------------------------------------");
            System.out.println("");
            words.add(toAdd);
        }
        return words;
    }

    private static String NumtoBin(long longitudCadena) {
        StringBuilder sb = new StringBuilder();
        while (longitudCadena > 0) {
            long r = longitudCadena % 2;
            sb.append(r);
            longitudCadena = longitudCadena / 2;
        }
        return sb.reverse().toString();
    }

    /*
    No entraban en un Int :)
     */
    public static long binaryToNum(String binary) {
        char[] numbers = binary.toCharArray();
        long result = 0;
        for (int i = numbers.length - 1; i >= 0; i--) {
            if (numbers[i] == '1') {
                result += Math.pow(2, (numbers.length - i - 1));
            }
        }
        return result;
    }

    private static String checkChunk(String chunk) {
        while (chunk.length() < 32) {
            chunk = "0" + chunk;
        }
        return chunk;
    }

    private static void dumpFile() throws IOException {
        String ruta = getDirectory();
        File f = new File(ruta);

        try {
            FileWriter fwriter = new FileWriter(f);
            BufferedWriter bwriter = new BufferedWriter(fwriter);
            bwriter.write(sb.toString());
            bwriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getDirectory() throws IOException {

        String NombreArchivo = "dumpCalculoHash.txt";
        File fileName = new File(NombreArchivo);

        JFileChooser explorer = new JFileChooser();
        explorer.setDialogTitle("Guardar como...");
        explorer.setSelectedFile(fileName);
        disableTextField(explorer.getComponents());

        int resultado = explorer.showSaveDialog(new JFrame());
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File file = explorer.getSelectedFile();
            return file.getAbsolutePath();
        } else {
            System.out.println("No se ha guardado archivo de ejecucion.");
            System.exit(0);
            return null;
        }
    }

    public static void disableTextField(Component[] comp) {
        for (int x = 0; x < comp.length; x++) {
            if (comp[x] instanceof JPanel) {
                disableTextField(((JPanel) comp[x]).getComponents());
            } else if (comp[x] instanceof JTextField) {
                ((JTextField) comp[x]).setEditable(false);
                return;
            }
        }
    }

}
