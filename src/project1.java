import java.io.*;
import java.net.URL;
import java.util.*;


public class project1 {
private static final String path="NFA2.txt";  /** FILE NAME GOES HERE COMMENTED LINES
                                                    IN BUFFERED READER IF UNCOMMENTED MAKE ACCESS
                                                    TO FILE BY GIVING DIRECTORY. TO TRY OTHER NFA FILES
                                                    JUST CHANGE PATH STRING TO FILENAME**/


    public static BufferedReader FileReader() {
        URL path2= project1.class.getResource(path);

        //System.out.println("Enter NFA text file location");
        boolean validFile = false;
        BufferedReader reader = null;
        File file = null;

        while (!validFile ) {
            try {
                /** COMMENT THIS LINE AND UNCOMMENT LINE BELOW IN CASE
                 PROGRAM CAN'T FIND FILE PATH STRING DOES NOT ACCEPT "" CHARACTERS**/
                try{
                    file = new File(path2.getFile());
                    //file = getFileFromStringPath();
                }
                catch (NullPointerException e){
                    System.out.println("CANNOT FIND FILE");
                }

                reader = new BufferedReader(new FileReader(file));
                validFile = true;

            } catch (FileNotFoundException e) {
                System.out.println("File not found");
                e.printStackTrace();
                break;

            }
        }


        return reader;
    }
    public static File getFileFromStringPath(){
        System.out.println("ENTER NFA FILE DIRECTORY");
        Scanner scan = new Scanner(System.in);
        String path = scan.nextLine();
        File file = new File(path);
        return file;
    }

    public static void print(ArrayList<String> a, String key){
        System.out.println(key);
        for(String x : a){
            System.out.println(x);
        }

    }
    public static void print(String a [][]){
        for (int i = 0; i < a[0].length; i++) {
            for (String[] strings : a) {
                System.out.print(strings[i] + "        ");
            }

            System.out.println();
        }
    }
    public static void print(String s,String key){
        System.out.println(key);
        System.out.println(s);
    }
    public static void print(ArrayList<ArrayList<String>> a){
        for (int i = 0; i < a.size(); i++) {
            for (int j = 0; j < a.get(0).size(); j++) {
                System.out.print(a.get(i).get(j)+"  ");
            }

            System.out.println();
        }
    }
    public static int indexSearcher(String s,ArrayList<String> a){
        int index=0;
            for(int i=0;i<a.size();i++){
                if (a.get(i).equals(s))
                    index = i;
            }
        return index+1;
    }
    public static String[][] arrayFiller(String[][] a){
        for (int i=0;i<a[0].length;i++){
            for(int j=0;j<a.length;j++)
                a[j][i]="Φ";
        }
        return a;
    }
    public static ArrayList<String> finalStateSearcher(ArrayList<String>nfafinal,ArrayList<String>dfastates){
        ArrayList<String> returnarray=new ArrayList<>();
        for (String x :nfafinal){
            for (String y : dfastates){
                for (char xs: x.toCharArray()){
                    for (char ys: y.toCharArray()){
                        if (xs==ys){
                            returnarray.add(y);
                        }
                    }
                }
            }
        }
        Set<String> returnset=new HashSet<String>();
        for (String x: returnarray){
            returnset.add(x);
        }

        returnarray.clear();

        for (String x : returnset){
            returnarray.add(x);
        }
        return returnarray;
    }
    public static String duplicateRemover(String s, int n){
        char[] str= s.toCharArray();
        HashSet<Character> hs = new LinkedHashSet<>(n - 1);
        for (char x : str)
            hs.add(x);

       return hs.toString().replaceAll("\\,|\\[|\\]|\\s", "");
    }
    public static String sortString(String s){
        char temp[] = s.toCharArray();
        Arrays.sort(temp);
        return new String(temp);
    }

    public static void main(String[] args) throws IOException {
        ArrayList<String> alphabet = new ArrayList<>();
        ArrayList<String> states = new ArrayList<>();
        String startstate = null;
        ArrayList<String> finalstate = new ArrayList<>();

        String indicator = "";
        BufferedReader reader = FileReader();
        String st;

        while ((st = reader.readLine()) != null) {
            if (st.equals("ALPHABET") || st.equals("START") || st.equals("FINAL") || st.equals("TRANSITIONS") || st.equals("END") || st.equals("STATES")) {
                indicator = st;
               // System.out.println("inside if: indicator" + indicator);
            }
            if (indicator.equals("ALPHABET") && !st.equals("ALPHABET")) {
                alphabet.add(st);
            } else if (indicator.equals("STATES") && !st.equals("STATES")) {
                states.add(st);
            } else if (indicator.equals("START") && !st.equals("START")) {
                startstate = st;
            } else if (indicator.equals("FINAL") && !st.equals("FINAL")) {
                finalstate.add(st);
            } else if (indicator.equals("TRANSITIONS") && st.equals("TRANSITIONS")) {
                break;
            }
        }
        //CONSTRUCTING NFA TRANSITION TABLE HERE

        String[][] NFAtable = new String[alphabet.size() + 1][states.size() + 1];
        NFAtable = arrayFiller(NFAtable);
        NFAtable[0][0] = " ";

        for (int i = 0; i < alphabet.size(); i++)
            NFAtable[i + 1][0] = alphabet.get(i);
        for (int i = 0; i < states.size(); i++)
            NFAtable[0][i + 1] = states.get(i);

        //print(NFAtable);
        while ((st = reader.readLine()) != null && !st.equals("END")) {
            //System.out.println("inside last");
            String state = st.split(" ")[0];
            String character = st.split(" ")[1];
            String transition = st.split(" ")[2];
            int stateindex = indexSearcher(state, states);
            int alphabetindex = indexSearcher(character, alphabet);
            //System.out.println(stateindex + "   " + alphabetindex);
            if (NFAtable[alphabetindex][stateindex].equals("Φ"))
                NFAtable[alphabetindex][stateindex] = transition;
            else
                NFAtable[alphabetindex][stateindex] += transition;

        }
       //print(NFAtable);
        //CREATING DFA TABLE
        ArrayList<String>DFAstates=new ArrayList<>();
        ArrayList<String>DFAtable=new ArrayList<>();
        DFAtable.add(NFAtable[0][1]+" "+alphabet.get(0)+" "+NFAtable[1][1]); //copy first
        DFAtable.add(NFAtable[0][1]+" "+alphabet.get(1)+" "+NFAtable[2][1]);
        DFAstates.add(NFAtable[0][1]);
        //System.out.println(DFAtable);
        for (int i =0;i<DFAtable.size();i++){
            String root= DFAtable.get(i).split(" ")[0];
            String transition= DFAtable.get(i).split(" ")[2];
            //System.out.println(transition);
            if (!DFAstates.contains(transition)){
                DFAstates.add(transition);

                for (int alp=0;alp<alphabet.size();alp++){
                    String left="";
                    String entry = "";
                    String s="";
                    ArrayList<String> temp=new ArrayList<>();
                     //B

                    for (int stsz=0;stsz<transition.length();stsz++){//split string
                        char tmp = transition.charAt(stsz);
                        String search = Character.toString(tmp);
                        entry +=NFAtable[alp+1][indexSearcher(search,states)];
                        temp.add(entry);
                        //System.out.println(search);

                        //DFAtable.add(entry);
                        //System.out.println(temp);
                    }
                    if (entry.contains("Φ")){
                        for (int k=0;k<entry.length();k++){
                            if(entry.charAt(k)!='Φ'){
                                s+=entry.charAt(k);
                                s=duplicateRemover(s,s.length());
                            }
                        }
                    }
                    else {
                        s=sortString(entry);
                        s=duplicateRemover(s,s.length());
                    }
                    if (s.equals("")){
                        s="Φ";
                        DFAtable.add(transition+" "+alphabet.get(alp)+" "+s);
                    }
                    else{
                        DFAtable.add(transition+" "+alphabet.get(alp)+" "+s);
                    }
                    //System.out.println(s);
                }
            }
        }
        for (int x=0;x<DFAtable.size();x++){
            String s=DFAtable.get(x);
            String root= s.split(" ")[0];
            String alp=s.split(" ")[1];
            String transition = s.split(" ")[2];
            if(root.equals("Φ")){
                transition="Φ";
                DFAtable.set(x,root+" "+alp+" "+transition);
            }
        }


        //System.out.println(DFAtable);
        //System.out.println(DFAstates);
        print(alphabet,"ALPHABET");
        print(DFAstates,"STATES");
        print(startstate,"START");
        print(finalStateSearcher(finalstate,DFAstates),"FINAL");
        print(DFAtable,"TRANSITIONS");
        System.out.println("END");



        }

        //print(DFAtable);
    }

