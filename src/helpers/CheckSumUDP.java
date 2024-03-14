package helpers;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class CheckSumUDP {

    public static long calculateChecksum(byte[] bytes) {
        Checksum checksum = new CRC32();
        checksum.update(bytes, 0, bytes.length);
        return checksum.getValue();
    }
}