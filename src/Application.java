import java.util.ArrayList;

public class Application {


    public static void main(String[] args) {
        ArrayList<Corredor> corredores = new ArrayList<Corredor>();
        Corrida corrida = new Corrida();
        corrida.addVoltasCorrida(10);
        for (int i = 1; i <= 10; i++) {
            Corredor c = new Corredor("CORREDOR " + i,corrida);
            corredores.add(c);
            corrida.adicionarCorredor(c);
        }


            for (Corredor c : corredores) {
                c.start();

            }
        for (Thread thread : corredores) {
            try {
                thread.join(); // Espera até que a thread termine
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
            corrida.imprimePodium();
        }




    public static class Corredor extends Thread {
      Corrida corrida;
      boolean finalizouCorrida = false;
      int qtdPontos = 0;

        public int getQtdPontos() {
            return qtdPontos;
        }

        public void setQtdPontos(int qtdPontos) {
            this.qtdPontos += qtdPontos;
        }

        public boolean isFinalizouCorrida() {
            return finalizouCorrida;
        }

        public void setFinalizouCorrida(boolean finalizouCorrida) {
            this.finalizouCorrida = finalizouCorrida;
        }

        public Corredor(String nome, Corrida corrida) {
            super.setName(nome);
            this.corrida = corrida;
        }

        @Override
        public void run() {

            try {
                while (corrida.corridaNumVoltas >0) {
                    corrida.iniciaCorrida(this);
                }

                } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

        }
    }



    public static class Corrida {
        ArrayList<Corredor> corredores = new ArrayList<Corredor>();
        volatile int pontuacao = 10;
        volatile int corridaNumVoltas;
        volatile boolean finalizouVoltas = false;
        StringBuilder podium = new StringBuilder();


        public void adicionarCorredor(Corredor c) {
            corredores.add(c);

        }

        public synchronized void iniciaCorrida(Corredor c) throws InterruptedException {

                Thread.sleep(300);
                int pontuacaoRef = pegarPontuacaoRefCorrida();
                c.setQtdPontos(pontuacaoRef);
                System.out.println(Thread.currentThread().getName()+"#, chegou em "+(11-pontuacaoRef)+"°");
            System.out.println(Thread.currentThread().getName()+"| já pontuou :"+c.getQtdPontos());
                Thread.sleep(300);
                c.setFinalizouCorrida(true);
                verificaSeTodosFinalizaramACorrida();





        }

        public synchronized void verificaSeTodosFinalizaramACorrida() throws InterruptedException {
            if(finalizaram()) {
                Thread.sleep(300);
                if(corridaNumVoltas>0){
                        System.out.println("Corrida #"+(11-corridaNumVoltas)+",sendo iniciada...");
                        System.out.println("Preparando corredores...");
                }
                notifyAll();
            } else {
                // Deixa o corredor(Thread) esperando para próxima corrida.

                wait();
            }

            //metodo apenas para printar status


        }

        public synchronized int pegarPontuacaoRefCorrida() {
            int pontuacaoReturn;
            if(pontuacao ==1 && corridaNumVoltas >0) {
                pontuacaoReturn = pontuacao;
                pontuacao = 10;
            } else {
                pontuacaoReturn = pontuacao;
                pontuacao--;
            }
            return pontuacaoReturn;
            }


        public  synchronized boolean finalizaram() {
            for (Corredor c : corredores) {
                if(!c.isFinalizouCorrida()) {
                    return false;
                }

            }
            resetaBooleanoStatusCorrida();
            corridaNumVoltas--;
            return true;
        }
        public void addVoltasCorrida(int qtdVoltas) {
            this.corridaNumVoltas = qtdVoltas;
        }
        public void imprimePodium() {
            System.out.println("Corridas foram encerradas...");
            System.out.println("Eis que o podium ficou assim :");
            System.out.println("------------------------------------------------------"+"\n");
            corredores.sort((o1, o2) -> Integer.compare(o2.qtdPontos,o1.qtdPontos));
            StringBuilder colocacao = new StringBuilder();
            int podium = 1;
            for (int j = 0;j<corredores.size();j++) {
                corredores.get(j);
                colocacao.append(corredores.get(j).getName()+"|"+podium+"°COLOCAÇÃO"+"|PONTUAÇÃO :"+corredores.get(j).qtdPontos+"\n");
                podium++;
            }
            System.out.println(colocacao.toString());
        }

        public synchronized void resetaBooleanoStatusCorrida() {
            for (Corredor c : corredores) {
                c.setFinalizouCorrida(false);
            }
        }
    }



}


