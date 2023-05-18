import java.io.*;
import java.lang.instrument.Instrumentation;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.Set;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    /*public static void main(String[] args)
    {
        Scanner scan = new Scanner(System.in);
        LSMTree bst = new LSMTree<Integer, Integer>();
        System.out.println("Binary Search Tree Test\n");
        char ch;
        do
        {
            System.out.println("\nBinary Search Tree Operations\n");
            System.out.println("1. insert ");
            System.out.println("2. delete");
            System.out.println("3. search");
            System.out.println("4. count nodes");
            System.out.println("5. check empty");

            int choice = scan.nextInt();
            switch (choice)
            {
                case 1 :
                    System.out.println("Enter integer element to insert");
                    bst.insert( scan.nextInt() , scan.nextInt() );
                    break;
                case 2 :
                    System.out.println("Enter integer element to delete");
                    bst.delete( scan.nextInt() );
                    break;
                case 3 :
                    System.out.println("Enter integer element to search");
                    System.out.println("Search result : "+ bst.search( scan.nextInt() ));
                    break;
                case 4 :
                    System.out.println("Nodes = "+ bst.countNodes());
                    break;
                case 5 :
                    System.out.println("Empty status = "+ bst.isEmpty());
                    break;
                default :
                    System.out.println("Wrong Entry \n ");
                    break;
            }
            System.out.print("\nPost order : ");
            bst.postorder();
            System.out.print("\nPre order : ");
            bst.preorder();
            System.out.print("\nIn order : ");
            bst.inorder();

            System.out.println("\nDo you want to continue (Type y or n) \n");
            ch = scan.next().charAt(0);
        } while (ch == 'Y'|| ch == 'y');
    }
*/
    /*public static void main(String args[]) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("example.bin"));
        Long long_num = 1597513634L;
        writer.write((int) InstrumentationAgent.getObjectSize(long_num));
        writer.write(long_num);
        writer.close();
        RandomAccessFile file = new RandomAccessFile("example.bin", "rw");
        file.seek(1);
        byte[] keyBytes = new byte[10];
        file.read(keyBytes);
        String key = new String(keyBytes, StandardCharsets.UTF_8);
        System.out.println(key);
        file.close();
    }*/

    /*public static void main(String args[]) throws IOException, ClassNotFoundException {
        FileOutputStream fos = new FileOutputStream("example.bin");
        DataOutputStream dos = new DataOutputStream(fos);
        String keystr = "Imthekey";
        String valstr = "Imtheval";
        byte[] keyBytes = keystr.getBytes("UTF-8");
        byte[] valueBytes = valstr.getBytes("UTF-8");
        int keySize = keyBytes.length;
        int valueSize = valueBytes.length;

        dos.writeInt(keySize);
        dos.writeInt(valueSize);
        dos.write(keyBytes);
        dos.write(valueBytes);
        dos.close();
        RandomAccessFile file = new RandomAccessFile("example.bin", "rw");
        file.seek(0);
        int key_size = file.readInt();
        int value_size = file.readInt();
        file.seek(8 + key_size);
        byte[] buffer = new byte[value_size];
        file.readFully(buffer);
        String str = new String(buffer, StandardCharsets.UTF_8);
        System.out.println(str);
        file.close();

    }*/
    public static void main(String args[]) throws IOException, InterruptedException {
        BitCask bitCask = new BitCask("/home/mahmoud/IdeaProjects/BitCask");
        String message = "{\n" +
                "\"station_id\": 1, // Long\n" +
                "\"s_no\": 1, // Long auto-incremental with each message per service\n" +
                "\"battery_status\": \"low\", // String of (low, medium, high)\n" +
                "\"status_timestamp\": 1681521224, // Long Unix timestamp\n" +
                "\"weather\": {\n" +
                "\"humidity\": 35, // Integer percentage\n" +
                "\"temperature\": 100, // Integer in fahrenheit\n" +
                "\"wind_speed\": 13, // Integer km/h\n" +
                "}\n" +
                "}";
        Set<Integer> s;


        for(int i=0; i<10000; i++){
            bitCask.write(String.valueOf(i), message);
            System.out.println("put message " + i + bitCask.getTree_size_in_bytes());
            if(i == 321){
                System.out.println("################################");
                System.out.println("################################");
                System.out.println("################################");
                System.out.println("################################");
                String x = bitCask.read("37");
                if(x == null){
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2");
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2");
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2");
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2");

                }else{
                    System.out.println("#!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    System.out.println("#!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    System.out.println(x);
                    System.out.println("#!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    System.out.println("#!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

                }
            }
            Thread.sleep(50);
        }
    }

}