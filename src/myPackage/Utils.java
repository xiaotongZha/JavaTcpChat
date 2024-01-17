package myPackage;

import java.io.Closeable;
import java.io.IOException;
import java.util.Properties;

public class Utils {
	public static void close(Closeable... targets) {
		for (Closeable target : targets) {
			try {
				if (target != null)
					target.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}