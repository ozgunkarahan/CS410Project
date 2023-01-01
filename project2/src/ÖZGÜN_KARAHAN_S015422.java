import java.io.*;
import java.net.URL;
import java.util.*;
class Rule{
    String head;
    ArrayList<String> rules = new ArrayList<>();
    public Rule(String head,String rule){
        this.head = head;
        this.rules.add(rule);
    }
    public Rule(String head){
        this.head= head;

    }
    public void addRule(String x){
        this.rules.add(x);
    }
    public void print(){
        System.out.print(this.head+"-> ");
        for(int i = 0; i< this.rules.size();i++){
            if(i == 0 ){
                System.out.print(this.rules.get(i)+ " ");
            }
            else{
                System.out.print("| "+this.rules.get(i)+" ");
            }
        }
        System.out.println();
    }

}

public class ÖZGÜN_KARAHAN_S015422 {

    private static final String path="G2.txt";  /** FILE NAME GOES HERE COMMENTED LINES
     IN BUFFERED READER IF UNCOMMENTED MAKE ACCESS
     TO FILE BY GIVING DIRECTORY. TO TRY OTHER NFA FILES
     JUST CHANGE PATH STRING TO FILENAME**/


    public static BufferedReader FileReader() {
        URL path2= ÖZGÜN_KARAHAN_S015422.class.getResource(path);

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
        System.out.println("ENTER CFG FILE DIRECTORY");
        Scanner scan = new Scanner(System.in);
        String path = scan.nextLine();
        File file = new File(path);
        return file;
    }
    public static Rule StringToRule(String s){
        String head = s.split(":")[0];
        String rule= s.split(":")[1];
        Rule newrule = new Rule(head,rule);
        return newrule;
    }
    public static void ArrayPrinter(ArrayList<Rule> s, String key, boolean isFinal){
        if(!isFinal){
            System.out.println(key);
            for(Rule x : s){
                x.print();
            }
        }
        else {
            System.out.println(key);
            for(Rule x : s){
                for (String rule : x.rules){
                    System.out.println(x.head+":"+rule);
                }

            }

        }

    }
    public static void ArrayPrinter(ArrayList<String> s, String key){
        System.out.println(key);
        for (String x :s){
            System.out.println(x);
        }
    }
    public static String TerminalRuleFinder(ArrayList<Rule> cfg, String rule){
        String temp = null;
        for (Rule x : cfg){
            if (x.rules.get(0).equals(rule)){
                temp = x.head;
            }
        }
        return temp;
    }



    public static void main(String[] args) throws IOException {

        ArrayList<Rule> CFG = new ArrayList<>();
        ArrayList<String> NonTerminals = new ArrayList<>();
        ArrayList<String> Terminals = new ArrayList<>();
        BufferedReader reader = FileReader();
        String start = null;
        String st, indicator = null;
        while ((st= reader.readLine())!= null) {
            if(st.equals("NON-TERMINAL") || st.equals("TERMINAL") || st.equals("RULES") || st.equals("START")){
                indicator=st;
            }
            if (indicator.equals("NON-TERMINAL") && !st.equals("NON-TERMINAL")){
                NonTerminals.add(st);
            }
            else if (indicator.equals("TERMINAL") && !st.equals("TERMINAL")){
               Terminals.add(st);

            }
            else if (indicator.equals("RULES") && !st.equals("RULES")) {
                Rule newrule= StringToRule(st);

                if(CFG.size()>0){
                    boolean Found = false;
                    for (int i = 0 ;i<CFG.size();i++){
                        if (newrule.head.equals(CFG.get(i).head)){
                            Found= true;
                            CFG.get(i).rules.add(newrule.rules.get(0));
                        }
                    }
                    if (!Found){
                        CFG.add(newrule);
                    }
                }
                else {
                    CFG.add(newrule);
                }

            } else if (indicator.equals("START") && !st.equals("START")) {
                start = st;
                break;
            }
        }
        //ArrayPrinter(CFG, "Before epsilon removal",false);
        //epsilon removal
        ArrayList<String> epsilonHeads = new ArrayList<>();
        for(int i = 0 ; i<CFG.size(); i++){
            if(CFG.get(i).rules.contains("e")){
                //System.out.println("epsilon found on "+ CFG.get(i).head);
                epsilonHeads.add(CFG.get(i).head);
                CFG.get(i).rules.remove("e");
            }
        }

        //ArrayPrinter(CFG,"after epsilon removal",false);
        for (int y = 0 ; y<CFG.size(); y++){
            for (int j = 0 ; j< epsilonHeads.size();j++){
                for (int x = 0; x<CFG.get(y).rules.size();x++){
                    if (CFG.get(y).rules.get(x).contains(epsilonHeads.get(j)) && !CFG.get(y).head.equals(start) && !epsilonHeads.contains(CFG.get(y).head) && CFG.get(y).rules.get(x).length() == 1){
                        System.out.println(CFG.get(y).head);
                        epsilonHeads.add(CFG.get(y).head);
                   }
                }
            }
        }

        //null production removal
        //System.out.println(epsilonHeads);
        //find epsilon heads in CFG
        for (int i =0; i<CFG.size();i++){
            for (int j = 0; j< CFG.get(i).rules.size();j++){
                for (int y =0 ; y< epsilonHeads.size();y++){
                    if (CFG.get(i).rules.get(j).contains(epsilonHeads.get(y))){
                        //System.out.println("FOUND EPSILON "+ epsilonHeads.get(y)+" HEAD ON " +CFG.get(i).head);
                        if (CFG.get(i).rules.get(j).length()==1){
                            //CFG.get(i).rules.remove(j);
                            //System.out.println("FOUND EPSILON "+ epsilonHeads.get(y)+" HEAD ON " +CFG.get(i).head);
                            epsilonHeads.add(CFG.get(i).head);
                        }
                        else {
                            String toRemove = epsilonHeads.get(y);
                            //System.out.println("to remove" +toRemove);
                            ArrayList<Integer> indexes = new ArrayList<>();
                            for (int x = 0 ; x<CFG.get(i).rules.get(j).length(); x++ ){
                                //System.out.println("CHAR "+(CFG.get(i).rules.get(j).charAt(x)));

                                if (toRemove.equals(Character.toString(CFG.get(i).rules.get(j).charAt(x)))){
                                    indexes.add(x);
                                    StringBuilder tmp = new StringBuilder(CFG.get(i).rules.get(j));
                                    tmp.deleteCharAt(x);
                                    CFG.get(i).rules.add(tmp.toString());
                                }
                            }
                            //System.out.println(CFG.get(i).rules.get(j));
                            //String tmp = CFG.get(i).rules.get(j).replace(epsilonHeads.get(y),"");
                            //CFG.get(i).rules.add(tmp);
                        }
                    }
                }

            }
        }
       // ArrayPrinter(CFG,"after null production removal",false);
        //remove duplicates
        for (Rule x : CFG){
            Set<String> tmp = new HashSet<>(x.rules);
            x.rules.clear();
            ArrayList<String> tmp2 = new ArrayList<>(tmp);
            x.rules= tmp2;
        }

       // ArrayPrinter(CFG,"after duplicate removal",false);
        //removing unit productions
        Set<Rule> UnitProduction = new HashSet<>();
        for (int i = 0 ; i<CFG.size();i++){
            for (int y = 0 ; y<CFG.get(i).rules.size();y++){
                for (String x: NonTerminals){
                    if (CFG.get(i).rules.get(y).equals(x)){
                        String unitprod= CFG.get(i).rules.get(y);
                        CFG.get(i).rules.remove(y);
                        //System.out.println(unitprod);
                        for (Rule rule : CFG){
                            if (rule.head.equals(unitprod)){
                                ArrayList<String> rulesin = new ArrayList<>(rule.rules);
                                Rule tmp = new Rule(CFG.get(i).head);
                                for (String rules : rule.rules){
                                    tmp.addRule(rules);
                                }

                                UnitProduction.add(tmp);
                                //System.out.println("tempppppp");
                                //tmp.print();

                            }
                        }

                    }
                }
            }
        }
       // ArrayPrinter(CFG,"BEFORE UNIT",false);
        //Adding unit productions
        //removing duplicates
        for (Rule y: UnitProduction){
            for (Rule x :CFG){
                if (x.head.equals(y.head)){
//                    System.out.println("if");
//                    x.print();
//                    y.print();
                    for (String newrule : y.rules){
                        x.addRule(newrule);

                    }
                }
            }
        }

        //ArrayPrinter(CFG,"AFTER UNIT",false);
        for (Rule x : CFG){
            Set<String> tmp = new HashSet<>(x.rules);
            x.rules.clear();
            ArrayList<String> tmp2 = new ArrayList<>(tmp);
            x.rules= tmp2;
        }

       // ArrayPrinter(CFG,"after unit production removal",false);

        int globalindex=0;
        int newRulesStartIndex = globalindex+ CFG.size();
        ArrayList<String> UsableNames = new ArrayList<>();
        for(char i = 'A'; i<='Z';i++ ){
            String tmp = Character.toString(i);
            if (!NonTerminals.contains(tmp) && !tmp.equals("Z")){
                UsableNames.add(tmp);
            }
        }
       // System.out.println(UsableNames);
        //System.out.println(UsableNames);
        //Create terminal rules
        ArrayList<Rule> terminalrules = new ArrayList<>();
        for (String x: Terminals){
            Rule tmp = new Rule(UsableNames.get(globalindex),x);
            globalindex++;
            CFG.add(tmp);
            terminalrules.add(tmp);
        }

        for (int x = 0; x<CFG.size()-globalindex;x++){
            for (int y = 0 ; y<CFG.get(x).rules.size();y++){
                for (String nonterm: NonTerminals){
                    for (String term: Terminals){
                        if (CFG.get(x).rules.get(y).contains(nonterm) && CFG.get(x).rules.get(y).contains(term) && CFG.get(x).rules.get(y).length() >1 ) {
                            //System.out.println("inside if");
                            for (int i = newRulesStartIndex; i< CFG.size(); i++){
                               if (CFG.get(i).rules.get(0).equals(term)){
                                   //System.out.println("inside if 2");
                                   CFG.get(x).rules.set(y,CFG.get(x).rules.get(y).replace(term,CFG.get(i).head));
                               }
                            }
                        }
                        else if (CFG.get(x).rules.get(y).contains(nonterm) && CFG.get(x).rules.get(y).length()>2 ) {
                            //System.out.println("inside if "+ CFG.get(x).rules.get(y));
                            int sizeofrule = CFG.get(x).rules.get(y).length();
                            if (sizeofrule %2 == 1){ ///ABBA
                                String[] a = CFG.get(x).rules.get(y).split("(?<=\\G..)");
                                for (int asz = 0 ; asz<a.length-1;asz++){
                                    Rule tmp2 = new Rule(UsableNames.get(globalindex), a[asz]);
                                    //UsableNames.get(globalindex);
                                    //String tmp3 = tmp2.rules.get(0)+a[a.length-1];
                                    //NonTerminals.add(tmp2.head);
                                    CFG.get(x).rules.set(y,tmp2.head+a[a.length-1]);
                                    globalindex++;
                                    CFG.add(tmp2);
                                }
                            }
                            else {
                                Rule tmp2 = new Rule(UsableNames.get(globalindex));
                                globalindex++;
                                String tmp3=CFG.get(x).rules.get(y);
                                tmp2.addRule(tmp3.substring(0,2));
                                CFG.get(x).rules.set(y,tmp3.substring(2,tmp3.length()-1));
                                CFG.add(tmp2);

                            }

                        }
                        else if (CFG.get(x).rules.get(y).contains(term)&& CFG.get(x).rules.get(y).length()==2){
                            //System.out.println("inside if" + CFG.get(x).rules.get(y));
                            //System.out.println(TerminalRuleFinder(CFG,term)+" RULE HEAD");
                            CFG.get(x).rules.set(y,CFG.get(x).rules.get(y).replace(term,TerminalRuleFinder(CFG,term)));

                        }


                    }

                }
                if (CFG.get(x).rules.get(y).length()>2){
                    //x=0;
                    //y=0;
                }
            }


        }
        //fixing new added
        for (int i=0;i<CFG.size();i++){
            for (int j=0;j<CFG.get(i).rules.size();j++){
                for (String x : Terminals){
                    if (CFG.get(i).rules.get(j).length()==2 && CFG.get(i).rules.get(j).contains(x)){

                        //System.out.println(CFG.get(i).rules.get(j));
                        //System.out.println(CFG.get(i).head);

                        String newhead=TerminalRuleFinder(CFG,x);

                        //System.out.println(newhead);
                        CFG.get(i).rules.set(j, CFG.get(i).rules.get(j).replace(x,newhead));

                    }
                }

            }
        }
        String oldstart = start;
        ArrayList<Rule> CNF =new ArrayList<>();
        Rule newStart = new Rule("Z");

        for (Rule x: CFG){
            if (start.equals(x.head)){
                for (String y:x.rules){
                    newStart.addRule(y);
                }
            }
        }

        CNF.add(newStart);
        CNF.addAll(CFG);
        NonTerminals.clear();
        start=CNF.get(0).head;
        for (Rule x : CNF){
            NonTerminals.add(x.head);
        }
        for (int i = 0;i<CNF.size();i++){
            for (int j = 0;j<CNF.get(i).rules.size(); j++){
                if (CNF.get(i).rules.get(j).contains(oldstart)){
                    CNF.get(i).rules.set(j,CNF.get(i).rules.get(j).replace(oldstart,start));
                }
            }
        }
        //ArrayPrinter(CNF,"\nCleaned up version\n",false);



        ArrayPrinter(NonTerminals,"\nNON-TERMINAL");
        ArrayPrinter(Terminals,"TERMINAL");
        ArrayPrinter(CNF,"RULES",true);
        System.out.println("START \n"+start);


        //ArrayPrinter(CNF,"after rename",false);
    }


}

