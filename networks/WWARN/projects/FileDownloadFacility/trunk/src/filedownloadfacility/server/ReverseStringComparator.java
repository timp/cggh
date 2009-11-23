package filedownloadfacility.server;

import java.util.Comparator;

/**
 * User: lee
 * Date: 01-Oct-2009
 * Time: 11:09:10
 */
public class ReverseStringComparator  implements Comparator<String> {

    // Implement the compare() method so that it
    // reverses the order of the string comparison.
    public int compare(String strA, String strB)
    {
        // Compare strB to strA, rather than strA to strB.
        return strB.compareTo(strA);
    }

}
