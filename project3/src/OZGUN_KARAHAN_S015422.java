import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
class TuringStates{
    String entryState;
    String read;
    String write;
    String move;
    String outState;
    int loopCount=0;

    public TuringStates(String x){
        ArrayList<String> TMState = new ArrayList<>();
        TMState = OZGUN_KARAHAN_S015422.StringDivider(x," ");
        this.entryState=TMState.get(0);
        this.read=TMState.get(1);
        this.write=TMState.get(2);
        this.move=TMState.get(3);
        this.outState=TMState.get(4);
    }
    public void print(){
        System.out.println(this.entryState+ " "+this.read+ " "+this.write + " " + this.move + " " + this.outState);
    }
    public void increaseLoop(){
        this.loopCount++;
    }
}
public class OZGUN_KARAHAN_S015422 {

    private static final String path="INPUT_OZGUN_KARAHAN_S015422.txt";  /** FILE NAME GOES HERE COMMENTED LINES
     IN BUFFERED READER IF UNCOMMENTED MAKE ACCESS
     TO FILE BY GIVING DIRECTORY. TO TRY OTHER TM FILES
     JUST CHANGE PATH STRING TO FILENAME**/


    public static BufferedReader FileReader() {
        URL path2= OZGUN_KARAHAN_S015422.class.getResource(path);

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
        System.out.println("ENTER TM FILE DIRECTORY");
        Scanner scan = new Scanner(System.in);
        String path = scan.nextLine();
        File file = new File(path);
        return file;
    }

    public static ArrayList<String> StringDivider(String x,String regex){

        return new ArrayList<>(Arrays.asList(x.split(regex)));
    }
    public static TuringStates StateFinder(ArrayList<TuringStates> TM, String start, String read ){
        TuringStates temp = null;
        for(TuringStates x : TM){
            if(x.entryState.equals(start) && x.read.equals(read)){
                temp = x;
            }
        }

        return temp;
    }
    public static int IndexUpdater(TuringStates x, int index){
        if (x.move.equals("R")){
            return index+1;
        }
        else
            return index-1;

    }

    public static void main(String[] args) throws IOException {
        ArrayList<String> TapeAlphabet = new ArrayList<>();
        ArrayList<String> States= new ArrayList<>();
        ArrayList<TuringStates> TM = new ArrayList<>();
        String StartState = null, EndState, Accept = null, Reject = null, Blank = null;
        ArrayList<String> Tape = new ArrayList<>();
        BufferedReader reader = FileReader();
        String st, indicator = null;
        while((st= reader.readLine())!= null){
            if(st.equals("TAPEALPHABET") || st.equals("BLANK") || st.equals("STATES") || st.equals("START")
                    || st.equals("ACCEPT") || st.equals("REJECT")|| st.equals("TMSTART") || st.equals("TMEND")){
                indicator=st;
            }
            if (indicator.equals("TAPEALPHABET") && !st.equals("TAPEALPHABET")){
                TapeAlphabet= StringDivider(st," ");
            }
            else if (indicator.equals("BLANK") && !st.equals("BLANK")){
                TapeAlphabet.add(st);
                Blank = st;

            }
            else if (indicator.equals("STATES") && !st.equals("STATES")) {
                States = StringDivider(st, " ");
            }
            else if (indicator.equals("START") && !st.equals("START")) {
                StartState=st;
            }
            else if (indicator.equals("ACCEPT") && !st.equals("ACCEPT")) {
                Accept=st;
            }
            else if (indicator.equals("REJECT") && !st.equals("REJECT")) {
                Reject=st;
            }
            else if (indicator.equals("TMSTART") && !st.equals("TMSTART")) {
                TuringStates temp = new TuringStates(st);
                TM.add(temp);
            }
            else if (indicator.equals("TMEND") && !st.equals("TMEND")) {
                Tape = StringDivider(st,"");
                Tape.add(Blank);
                break;
            }
        }
        ArrayList<String> TraversedStates = new ArrayList<>();
        int tapeindex = 0;
        ArrayList<String>oldtape = new ArrayList<>();
        TuringStates starting = StateFinder(TM,StartState,Tape.get(tapeindex));
        Tape.set(tapeindex,starting.write);
        tapeindex=IndexUpdater(starting,tapeindex);
        TraversedStates.add(starting.entryState);
//        starting.print();
//        System.out.println(Tape);
//        System.out.println(tapeindex);
        String AccOrRej = null;

        class LoopCheck{
            String entryState;
            ArrayList<String> tape;
            int index;
            public LoopCheck(String entryState, ArrayList<String> tape, int index){
                this.entryState=entryState;
                this.tape=tape;
                this.index = index;
            }
        }
        ArrayList<LoopCheck> loophistory = new ArrayList<>();
        try{
            while(true){
//                System.out.println("---------------------------");
//                System.out.println("TAPE BEFORE:     "+Tape);
//                System.out.println("index:  "+tapeindex);
                starting = StateFinder(TM,starting.outState, Tape.get(tapeindex));
                Tape.set(tapeindex,starting.write);
                tapeindex=IndexUpdater(starting,tapeindex);
                TraversedStates.add(starting.entryState);
//                starting.print();
                starting.increaseLoop();
                //System.out.println("TAPE AFTER:       "+Tape);
                if (starting.outState.equals(Reject) || starting.outState.equals(Accept)){
                    if (starting.outState.equals(Reject)){
                        TraversedStates.add(Reject);
                        AccOrRej= "Reject";
                        break;
                    }
                    else{
                        TraversedStates.add(Accept);
                        AccOrRej= "Accept";
                        break;
                    }
                }
//                else if (TraversedStates.contains(starting.outState) && tapeindex<Tape.size()) {
//                    boolean check = false;
//                    for (int i = 0;i<loophistory.size();i++){
//
//                        if (loophistory.get(i).tape.equals(Tape) && loophistory.get(i).entryState.equals(starting.entryState) && loophistory.get(i).index == tapeindex){
//                            check= true;
//                            System.out.println("LOOP HERE");
//                            starting.print();
//                            AccOrRej="Looped";
//                            break;
//                        }
//                    }
//                    if (check){
//                        break;
//                    }
//
//                }
                else if (starting.loopCount>1000) {
                    AccOrRej="Looped";
                    break;

                }
                LoopCheck tmp = new LoopCheck(starting.entryState,Tape, tapeindex);
//                System.out.println("Saving "+tmp.tape + " "+ tmp.entryState + " " + tapeindex);
                loophistory.add(tmp);

            }

        }
        catch (Exception e){
            System.out.println(e);
        }
//        System.out.println("LOOP HISTORY");
//        for (int i = 0; i<loophistory.size();i++){
//
//            System.out.println(loophistory.get(i).entryState +" "+ loophistory.get(i).index);
//            System.out.println(loophistory.get(i).tape);
//        }

        System.out.println("ROUT: "+ TraversedStates);
        System.out.println("Result: " + AccOrRej);


    }
}

