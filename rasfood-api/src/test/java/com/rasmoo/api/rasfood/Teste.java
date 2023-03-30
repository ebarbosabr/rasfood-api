package com.rasmoo.api.rasfood;

interface Executable {
    int execute( int a);
}
class Runner {
    public void run(Executable e) {
        System.out.println("A. Executing code block...");
        int value = e.execute(12);
        System.out.println("B. Return value is:" + value);
    }
}
public class Teste {

    public static void main(String[] args) {
        Runner runner = new Runner();
        runner.run(new Executable() {
            public int execute(int a) {
                System.out.println("C. Hello There!!!");
                return 7;
           }
        });
        System.out.println("==========================");
        runner.run((a) -> {
            System.out.println("Hello there." + a);
            return 1+a;
        });
    }
}
