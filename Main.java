package Server;


        import java.io.IOException;
        import java.net.ServerSocket;
        import java.net.Socket;
        import java.util.logging.*;

public class Main {

    public static void main(String[] args) {
        int port=8080,level=-1;
        if (args.length>0){
            port=Integer.valueOf(args[0]);
        }
        if (args.length>1){
            level=Integer.valueOf(args[1]);
        }
        try {
            FileHandler hand = new FileHandler("vk.log");
            Logger log = Logger.getLogger("log_file");
            log.addHandler(hand);
            MultiThreadsServer server = new MultiThreadsServer(port,log,level);
            server.run();
        } catch (IOException e) {
            System.out.println(e);
        }




    }
}

class MultiThreadsServer implements Runnable{
    private int port = 8080;
    private Thread running = null;
    private ServerSocket serverSock = null;
    private Logger log = null;
    private int level = -1;


    MultiThreadsServer(int port, Logger log, int level){
        this.port = port;
        this.log = log;
        this.level = level;
    }

    public void run(){
        synchronized(this){
            this.running = Thread.currentThread();
        }
        if (level==1){
            log.info("Server Starting");
        }
        openSocket();

        while(true){
            Socket clientSocket = null;
            try{
                clientSocket=this.serverSock.accept();
                if (level>0)
                    log.info("New connection"); // everything is ok , a new user has connected

            } catch (IOException e){
                if (level>0) {
                    log.warning("A user has been denied");
                }
                throw new RuntimeException("Something went wrong");

            }
            new Thread(
                    new RequestHandlerThread(
                            clientSocket,log,level)
            ).start();
        }

    }
    public void stop(){
        try{
            this.serverSock.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void openSocket(){
        try {
            this.serverSock = new ServerSocket(this.port);
            System.out.println("Listening on port: "+this.port);
            if (level>0){
                log.info("Server Listening on port "+this.port); //everything is ok, just say what port we are listening to.
            }

        } catch (IOException e ){
            if (level>=3){
                log.warning("Something went wrong when opening the socket with this port "+this.port);    //log saying what port could not be opened
                //when an error is thrown, we need to know what
                //port could not be opened.
            }
            throw new RuntimeException("Cannot open port "+port, e);

        }
    }
}