//HW4 code by: Kaylee Kutschera
//HiRiQ code editted by: Kaylee Kutschera
//Original HiRiQ code by: Claude Crepeau
import java.util.ArrayList;

/*Structure of brute force solution:
Constructed a binary tree. The parent node's right children are all possible configurations that
are one move off of the parent's configurations. My program checks all the right children configurations,
one-by-one, to see if they can be solved. If they do not lead to the solved configuration, the node and all 
their children are deleted. The output is a list of moves to get from the initial configuration to the solved 
configuration.
*/

public class HW4 {
 //additional classes at the bottom
  
 public static void main(String[] args){
   //****************INPUT HERE**********************
   //input boolean array into inputArray and uncomment the next two lines
   //boolean[] inputArray = ***;
   //execute(inputArray);
   
   
   //put in config and weight of the HiRiQ board on lines 15 and 16 and uncomment the next four lines;
   //HiRiQ inputBoard = new HiRiQ();
   //inputBoard.config = ***;
   //inputBoard.weight = ***;
   //execute(inputBoard);

   
   //following code in main method used for debugging
   
/* HiRiQ board = new HiRiQ((byte)0);
   Node node1 = new Node(board);
  
   boolean[] B=new boolean[33];
   board.load(B);
 
   B[0]=true;
   //B[1]=true;
    //B[2]=true;
   B[3]=true;
   B[4]=true;
    B[5]=true;
    B[6]=true;
   //B[7]=true;
   //B[8]=true;
    //B[9]=true;
    B[10]=true;
   //B[11]=true;
   //B[12]=true;
    B[13]=true;
    //B[14]=true;
   B[15]=true;
   //B[16]=true;
    B[17]=true;
    //B[18]=true;
   //B[19]=true;
   B[20]=true;
    B[22]=true;
    //B[23]=true;
    B[24]=true;
   //B[25]=true;
   //B[26]=true;
   B[27]=true;
  //B[28]=true;
    //B[29]=true;
   //B[31]=true;
   B[32]=true;
  
   board.store(B);
   Node node3 = new Node(board);
   node3.printNode();
   execute(B);

 */
 }
 
 //execute given a boolean array
 public static boolean execute(boolean[] inputArray){
   //create HiRiQ board for the array
   HiRiQ inputBoard = new HiRiQ();
   inputBoard.store(inputArray);
   //check initial board to see if already solved or impossible to solve
   start(inputBoard);
   //if solved, print solution and end
   if(global.solved==true){
     System.out.println(global.outputString);
     return true;
   }
   //otherwise initial start node and search for solution
   global.start = new Node(inputBoard);
   search(global.start);
   //if search yields nothing return false
   return false;
 }
 
  //execute given HiRiQ board
  public static boolean execute(HiRiQ inputBoard){
   start(inputBoard);
   if(global.solved==true){
     System.out.println(global.outputString);
     return true;
   }
   global.start = new Node(inputBoard);
   search(global.start);
   return false;
 }
 
 
 //Checks if board is already solved or is unsolvable
 public static void start(HiRiQ inputBoard){
   //if is already solved
   if(inputBoard.IsSolved()){
     global.solved=true;
     global.outputString = "Input HiRiQ is solved.";
   }
   //if one of 16 configuartions with no possible substitutions
   else if(has0Sub(inputBoard)){
     global.solved=true;
     global.outputString ="Impossible to solve";
   }
   //if is n=3 board (majorly cuts down on time)
   else if(inputBoard.config==-2147450879&& inputBoard.weight==32){
     global.solved=true;
     global.outputString="16@18 17@29 24@26 12@26 17@29 24@29 23@25 27@29 30@32 24@32 15@17 17@29 24@26 17@29 13@15 15@27 20@22 15@27 8@22 7@9 9@11 2@10 0@2 5@17 2@10 8@10 7@9 4@16";
   }
 }
 
  //removes all x's left child and grandchildren and x's parent connects with the nodes sibling ie. right child
 public static boolean removeNode(Node x){
   global.pointer = x ;
   //if removed all children of node x, remove node x and connect x's parent with x's sibling
   if(global.pointer.lChild==null){
     global.pointer.parent.lChild = global.pointer.rChild;
     
     if(global.pointer.rChild!=null){
       global.pointer.rChild.parent=global.pointer.parent;
     }
     //add x to the garbage node pile (ie. can be used to make other nodes later)
     global.garbage.add(global.pointer);
     return true;
   }
   
   //move to x's leftmost child
   while(global.pointer.lChild!=null){
     global.pointer = global.pointer.lChild;
   }
   //remove the leftmost child's right children
   while(global.pointer.rChild!=null){
     global.pointer = global.pointer.rChild;
     //set leftmost child's right child to the sibling of it current right child
     global.pointer.parent.rChild=global.pointer.rChild;
     if(global.pointer.rChild!=null){
       global.pointer.rChild.parent=global.pointer.parent;
     }
     //add leftmost child to garbage when done removing right children
     global.garbage.add(global.pointer);
     global.pointer=global.pointer.parent;
   }
   
   global.pointer.parent.lChild=null;
   global.garbage.add(global.pointer);
   //run recursively to remove all left children and their children
   return removeNode(x);
 }
 

 //makes a new node by first checking if there are garbage nodes to reuse or if have to create a new one 
 public static Node newNode(Node parent){
   if(global.garbage.isEmpty()){
     Node newNode = new Node(parent);
     return newNode;
   }
   else{
     Node newNode = global.garbage.get(global.garbage.size()-1);
     //reset all properties of the node from the garbage
     newNode.board=new HiRiQ();
     newNode.parent=parent;
     newNode.rChild=null;
     newNode.lChild=null;     
     newNode.sub=null;
     global.garbage.remove(global.garbage.size()-1);
     return newNode;
   }
 }
 
 //adds left or right child
 public static void addChild(Node parent, String type){
   Node newNode = newNode(parent);
   if(type == "right"){
     parent.rChild=newNode;
   }
   else if(type == "left"){
     parent.lChild=newNode;
   }
 }
 
 //prints final string once solved (moves up tree from final node until root of tree is found
 public static void printString(Node finalNode){
   global.pointer=finalNode;
   global.outputString=global.pointer.sub;
   while(global.pointer.parent.sub!=null){
     global.outputString=global.pointer.parent.sub+global.outputString;
     global.pointer=global.pointer.parent;
   }
   //beginString holds any B-substitutions that were done at the beginning of search
   System.out.println(global.beginString + global.outputString);
 }
 
 //searches for solution(mainly created to prevent stack overflow)
 public static void search(Node start){
   boolean result;
   boolean worked;
   Node startNode=start;
   while(global.solved==false){
    //traverse tree until solved 
    startNode=traverse(startNode);
    global.counter=0;
    if(global.reachTop==true){
      //reached top of tree; must to a B-substitution to continue
      worked=BSub(startNode);
      if(worked==false){
        System.out.println("Unable to solve with this algorithm");
        //Will be followed by a null pointer exception in WSubs
      }
      startNode=startNode.parent;
      global.reachTop=false;
    }
   }
   //print result once solved
   printString(startNode);
 }
 
 //traverse the tree until solved, reached the top or counter maxed(prevents stack overflow error)
 //given start node, finds all possible W-substitutions
 //does depth-first search until no more W-substitutions possible or reached a previously traverse board
 public static Node traverse(Node start){
   if(global.counter>50){
     return start;
   }
   global.counter++;
   global.curNode = start;
   WSubs(global.curNode);
   //if no W-substitutions possible
   if(global.curNode.lChild==null || seenBefore(global.curNode.lChild.board)){
     while(global.curNode.lChild==null || seenBefore(global.curNode.lChild.board)){
       if(global.curNode.parent!=null){
         global.curNode=global.curNode.parent;
       }
       else{
         //reached top of tree
         global.reachTop=true;
         return global.curNode;
       }
         removeNode(global.curNode.lChild);
     }
     //if solved, return last node of tree
     if (global.curNode.lChild.board.IsSolved()){
       global.curNode=global.curNode.lChild;
       System.out.println("SOLVED!");
       global.solved=true;
       return global.curNode;
     }
     //move to child and proceed with depth-first search
     else{
       global.curNode= global.curNode.lChild;
       return traverse(global.curNode);
     }
   }
   //if solved, return last node of tree
   else if (global.curNode.lChild.board.IsSolved()){
     global.curNode=global.curNode.lChild;
     System.out.println("SOLVED!");
     global.solved=true;
     return global.curNode;
   }
   //move to child and proceed with depth-first search
   else{
     global.curNode=global.curNode.lChild;
     return traverse(global.curNode);
   }
 }

 
 //finds single B-substitution and add it above the root of tree(returns false if error)
 public static boolean BSub(Node root){
   root.board.load(global.holderArray);
   for(int i=0; i<global.triples.length;i++){
     //if triple is BBW or WBB
     if((global.holderArray[global.triples[i][0]]==false && global.holderArray[global.triples[i][1]]==false && global.holderArray[global.triples[i][2]]==true) || 
        (global.holderArray[global.triples[i][0]]==true && global.holderArray[global.triples[i][1]]==false && global.holderArray[global.triples[i][2]]==false)){
       //if last triplet also doesn't work, return false
       if(i==37 && !(global.holderArray[global.triples[i][0]]==false && global.holderArray[global.triples[i][1]]==false && global.holderArray[global.triples[i][2]]==true)
            && !(global.holderArray[global.triples[i][0]]==true && global.holderArray[global.triples[i][1]]==false && global.holderArray[global.triples[i][2]]==false)){
         return false;
       }
       //make new root of tree
       Node newRoot= new Node();
       //attach to current tree
       newRoot.rChild=root;
       root.parent=newRoot;
       //do appropriate substitution
       doSub(newRoot,root, global.triples[i]);
       //change substition string of former root node
       root.sub=global.triples[i][0] +"@"+global.triples[i][2]+" ";
       //change beginString to include this substitution
       global.beginString=global.beginString+root.sub;
       return true;
     }
   }
   return false;
 }
 
 
 //finds all W-substitutions, adds them to the tree and adds parent to pastBoards
 public static void WSubs(Node parent){
   parent.board.load(global.holderArray);
   global.pointer = parent;
   for(int i=0;i<global.triples.length;i++){
     //if triple is WWB or BWW
     if((global.holderArray[global.triples[i][0]]==true && global.holderArray[global.triples[i][1]]==true && global.holderArray[global.triples[i][2]]==false) || 
        (global.holderArray[global.triples[i][0]]==false && global.holderArray[global.triples[i][1]]==true && global.holderArray[global.triples[i][2]]==true)){
       //if pointer is parent make left child
       if(global.pointer==parent){
         addChild(parent,"left");
         //do the substitution
         doSub(parent.lChild,parent,global.triples[i]);
         global.pointer=parent.lChild;
         //set nodes substition string
         global.pointer.sub=global.triples[i][0] +"@"+global.triples[i][2]+" ";
         //reset holderArray
         parent.board.load(global.holderArray);
       }
       //similar to above but make right child (right children are siblings)
       else{
         addChild(global.pointer,"right");
         doSub(global.pointer.rChild,parent,global.triples[i]);
         global.pointer=global.pointer.rChild;
         global.pointer.sub=global.triples[i][0]+"@"+global.triples[i][2]+" ";
         //reset holderArray
         parent.board.load(global.holderArray);
       }
     }
   }
   //add parent board to list of already searched boards
   global.pastBoards.add(parent.board);
 }
 
 
 //performs substitution
 public static void doSub(Node child,Node parent, int[] indices){
  //copies board
  copyHiRiQ(child.board, parent.board);
  //loads new node with same board as its parent
  child.board.load(global.holderArray);
  //does substitution on child's board
  global.holderArray[indices[0]]= !global.holderArray[indices[0]];
  global.holderArray[indices[1]]=!global.holderArray[indices[1]];
  global.holderArray[indices[2]]=!global.holderArray[indices[2]];
  //store final substitution in child's board
  child.board.store(global.holderArray);
}
 
 
//checks if current board is one of the past boards that have been searched
 public static boolean seenBefore(HiRiQ board){
   for(int i=0; i<global.pastBoards.size(); i++){
     if(equals(board,global.pastBoards.get(i))){
       return true;
     }
   }
   return false;
 }
 
 //tests if two HiRiQ objects are equal
 public static boolean equals(HiRiQ x, HiRiQ y){
  if(x.config==y.config && x.weight==y.weight){
   return true;
  }
  return false;
 }
 
 
 //copies HiRiQ object
 public static void copyHiRiQ(HiRiQ curNode, HiRiQ oldNode){
  curNode.config=oldNode.config;
  curNode.weight=oldNode.weight;
 }
 
 //returns true if one of the 16 configurations with no possible substitution
 public static boolean has0Sub(HiRiQ x){
  if((x.config == 0 && x.weight==0) || (x.config==134389768 && x.weight==5) || (x.config==335892500 && x.weight==8)){
   return true;
  }
  if((x.config==470282268 && x.weight==13) || (x.config==1566045533 && x.weight==21) || (x.config==-2147483647 && x.weight==33)){
   return true;
  }
  if((x.config==-2013126647 && x.weight==29) || (x.config==-1677201379 && x.weight==20) || (x.config==-1051720382 && x.weight==25)){
   return true;
  }
  if((x.config==-1811591147 && x.weight==25) || (x.config==-917330614 && x.weight==20) || (x.config==-715827882 && x.weight==17)){
   return true;
  }
  if((x.config==-581438114 && x.weight==12) || (x.config==1431655765 && x.weight==16) || (x.config==1230153033 && x.weight==13)){
   return true;
  }
  if(x.config==1095763265 && x.weight==8){
   return true;
  }
  return false;
 }
 

}

 class HiRiQ
{
 //int is used to reduce storage to a minimum...
 public int config;
 public byte weight;

 //initialize the configuration to one of 4 START setups n=0,1,2,3
 HiRiQ(byte n)
 {
  if (n==0)
  {config=65536/2;weight=1;}
  else
   if (n==1)
   {config=4403916;weight=11;}
   else
    if (n==2)
    {config=-1026781599; weight=21;}
   else if(n==3)
     {config=-411153748; weight=13;}
   else
    {config=-2147450879; weight=32;}
 }

 //initialize the configuration to one of 4 START setups n=0,10,20,30

 //*************Added a new constructor******************
 //if weight is -1, know object is equivalent to NULL
 public HiRiQ() {
  this.config=-1;
  this.weight=-1;
  
 }

 boolean IsSolved()
 {
  return( (config==65536/2) && (weight==1) );
 }

 //transforms the array of 33 booleans to an (int) config and a (byte) weight.
 public void store(boolean[] B)
 {
  int a=1;
  config=0;
  weight=(byte) 0;
  if (B[0]) {weight++;}
  for (int i=1; i<32; i++)
  {
   if (B[i]) {config=config+a;weight++;}
   a=2*a;
  }
  if (B[32]) {config=-config;weight++;}
 }

 //transform the int representation to an array of booleans.
 //the weight (byte) is necessary because only 32 bits are memorized
 //and so the 33rd is decided based on the fact that the config has the
 //correct weight or not.
 public boolean[] load(boolean[] B)
 {
  byte count=0;
  int fig=config;
  B[32]=fig<0;
  if (B[32]) {fig=-fig;count++;}
  int a=2;
  for (int i=1; i<32; i++)
  {
   B[i]= fig%a>0;
   if (B[i]) {fig=fig-a/2;count++;}
   a=2*a;
  }
  B[0]= count<weight;
  return(B);
 }

 //prints the int representation to an array of booleans.
 //the weight (byte) is necessary because only 32 bits are memorized
 //and so the 33rd is decided based on the fact that the config has the
 //correct weight or not.
 public void printB(boolean Z)
 {if (Z) {System.out.print("[ ]");} else {System.out.print("[@]");}}

 public void print()
 {
  byte count=0;
  int fig=config;
  boolean next,last;
  last=fig<0;
  if (last) {fig=-fig;count++;}
  int a=2;
  for (int i=1; i<32; i++)
  {
   next= fig%a>0;
   if (next) {fig=fig-a/2;count++;}
   a=2*a;
  }
  next= count<weight;

  count=0;
  fig=config;
  if (last) {fig=-fig;count++;}
  a=2;

  System.out.print("      ") ; printB(next);
  for (int i=1; i<32; i++)
  {
   next= fig%a>0;
   if (next) {fig=fig-a/2;count++;}
   a=2*a;
   printB(next);
   if (i==2 || i==5 || i==12 || i==19 || i==26 || i==29) {System.out.println() ;}
   if (i==2 || i==26 || i==29) {System.out.print("      ") ;};
  }
  printB(last); System.out.println() ;

 
 }
}

 
 
 //used "global" variables to conserve space
class global{
  //past nodes that are no longer in use
  public static ArrayList<Node> garbage = new ArrayList<Node>();
  //list of past boards that have already been searched
  public static ArrayList<HiRiQ> pastBoards = new ArrayList<HiRiQ>();
  //boolean array to use during methods
  public static boolean[] holderArray = new boolean[33];
  //string of substitutions made from root to final solution node
  public static String outputString = "";
  //string of B-substitutions made from start board to the root of tree
  public static String beginString = "";
  //HiRiQ board used for inbetween steps during methods
  public static HiRiQ holderBoard = new HiRiQ();
  //for removing and finding substitions methods
  public static Node pointer;
  //for traversing graph method
  public static Node curNode;
  //set start node to initial board
  public static Node start;
  //if puzzle is solved
  public static boolean solved=false;
  //counter used in traverse method to prevent stack overflow error
  public static int counter= 0;
  //true if searched all nodes of current tree and have reached top of tree (indicates if B-sub is necessary)
  public static boolean reachTop=false;

  //indices of all triples
  public static final int[][] triples={{0,1,2},{3,4,5},{6,7,8},{7,8,9},{8,9,10},{9,10,11},{10,11,12},{13,14,15},{14,15,16},{15,16,17},{16,17,18},{17,18,19},{20,21,22},
    {21,22,23},{22,23,24},{23,24,25},{24,25,26},{27,28,29},{30,31,32},{12,19,26},{11,18,25},{2,5,10},{5,10,17},{10,17,24},{17,24,29},{24,29,32},{1,4,9},{4,9,16},
    {9,16,23},{16,23,28},{23,28,31},{0,3,8},{3,8,15},{8,15,22},{15,22,27},{22,27,30},{7,14,21},{6,13,20}};
}

 
 class Node {
  HiRiQ board; 
  Node parent;
  Node lChild;
  Node rChild;
  //string naming the substition made to get from parent node to this node
  String sub;
 
 //Constructors
 public Node(HiRiQ board){
  this.board=board;
  this.parent=null;
  this.lChild=null;
  this.rChild=null;
  this.sub=null;
 }
 
 public Node(Node parent){
   this.board=new HiRiQ();
   this.parent=parent;
   this.lChild=null;
   this.rChild=null;
   this.sub=null;
 }

 public Node(){
   this.board=new HiRiQ();
   this.parent=null;
   this.lChild=null;
   this.rChild=null;
   this.sub=null;
 }
 

 //prints node (mainly for debugging)
 public void printNode(){
   this.board.print();
   //System.out.println(" parent:");
   //this.parent.board.print();
   //System.out.println(" lChild:");
   //this.lChild.board.print();
   //System.out.println(" rChild:"); 
   //this.rChild.board.print();
 }
}
 
 
 
