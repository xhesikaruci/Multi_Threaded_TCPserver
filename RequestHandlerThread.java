package Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Logger;

public class RequestHandlerThread implements Runnable {

    protected Socket clientSocket = null;
    private static Logger log =null;
    private static Integer level;


    public RequestHandlerThread(Socket clientSocket,Logger log,int level) {
        this.clientSocket = clientSocket;
        this.log=log;
        this.level=level;

    }

    public void run() {
        try {
            InputStream input = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();
            PrintStream out = new PrintStream(output);
            int bytesread;
            Double num=0.,num2=1.,fibonacci=0.;
            byte[] buff= new byte[1024];
            for (int loop = 0; loop < 500; loop ++)
            {
                fibonacci = num + num2;
                num = num2;
                num2 = fibonacci;
            }

            out.println(fibonacci.toString());
            clientSocket.close();


        } catch (IOException e) {
            //report exception somewhere.
            log.warning("the socket has been closed");

        }
    }
}
