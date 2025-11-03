package analisadorSintatico;

public class MainTree {
    public static void main(String[] args){
        Node nodeA= new Node("A");
        Node nodeB= new Node("B");
        Node nodeC= new Node("C");
        Node nodeD= new Node("D");
        Node nodeE= new Node("E");
        Node nodeF= new Node("F");
        nodeA.addNode(nodeB);
        nodeA.addNode(nodeC);
        nodeA.addNode(nodeD);
        nodeC.addNode(nodeE);
        nodeC.addNode(nodeF);

        Tree tree= new Tree(nodeA);

        tree.preOrder();//imprime empreordem
        tree.printCode(); //imprimeas folhas(codigo)
        tree.printTree(); //imprimeaarvore
    }
}
