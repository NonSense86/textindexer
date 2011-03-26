package at.tuwien.ir.textindexer.utils;

import java.io.File;

public final class TestUtilities {

    public static boolean deleteFolder(final File path) {
        if (path.exists()) {
            final File[] files = path.listFiles();
            for (final File f : files) {
                if (f.isDirectory()) {
                    TestUtilities.deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        
        return path.delete();
    }
    
    private TestUtilities() {
        
    }
}
