import java.util.Random;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class cpuspinner {
    public static class ParallelTask implements Runnable {
        private final Random rng = new Random();
        private boolean stopped = false;

        public void stop() {
            this.stopped = true;
        }
        
        @Override
        public void run() {
            while (!this.stopped) {
                try {
                    char[] alphabet = "abcdefghijklmnopqrstuvwxyz123456789".toCharArray();
                    char[] randBuf = new char[2048 * 1024];
                    for (int i = 0; i < 2048 * 1024; i++) {
                        randBuf[i] = alphabet[this.rng.nextInt(alphabet.length)];
                    }
                    String str = randBuf.toString();
                    MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                    messageDigest.update(str.getBytes());
                    messageDigest.digest();
                    str = null;
                    System.gc();
                } catch (NoSuchAlgorithmException e) {
                    return;
                }
            }

        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java cpuspinner <numthreads>\nExample usage: java cpuspinner 4");
            return;
        }
        int iarg = Integer.parseInt(args[0]);
        System.out.printf("Hashing 2mb buffer in %d threads\n", iarg);
        System.out.println("Press Ctrl+C to exit.");
        for (int i=0; i < iarg; i++) {
            Thread t = new Thread(new ParallelTask());
            t.start();
        }
    }
}
