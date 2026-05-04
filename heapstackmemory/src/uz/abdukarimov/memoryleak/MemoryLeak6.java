package uz.abdukarimov.memoryleak;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

// Sabab 6 — Connection/Stream yopilmasligi
public class MemoryLeak6 {
    // ❌ MUAMMO: Connection yopilmaydi
    public void readData1() throws Exception {
        Connection conn = DriverManager.getConnection("url");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT ...");
        // exception bo'lsa → close() hech qachon chaqirilmaydi!
        conn.close(); // bu qatorga yetib kelmasligi mumkin
    }

    // ✅ YECHIM: try-with-resources
    public void readData() throws Exception {
        try (Connection conn = DriverManager.getConnection("url");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT ...")) {
            // ishlash
        } // AutoCloseable — hammasi avtomatik yopiladi
    }
}
