package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import Helper.DBConnection;

public class SinavGoruntulemeGUI extends JFrame {
    private JTable sinavTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    private Connection connection;
    private JButton btnSil, btnGuncelle;

    public SinavGoruntulemeGUI() {
        setTitle("Sınav Görüntüleme Ekranı");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        connection = DBConnection.getConnection();

        // Table model
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Ders Adı");
        tableModel.addColumn("Öğrenci Sayısı");
        tableModel.addColumn("Sınav Tarihi");
        tableModel.addColumn("Başlangıç Saati");
        tableModel.addColumn("Bitiş Saati");
        tableModel.addColumn("Salon Adları");
        tableModel.addColumn("Gözetmen Adları");

        sinavTable = new JTable(tableModel);
        scrollPane = new JScrollPane(sinavTable);

        // Butonlar
        JPanel buttonPanel = new JPanel();
        btnSil = new JButton("Sınavı Sil");
        btnGuncelle = new JButton("Sınavı Güncelle");
        buttonPanel.add(btnSil);
        buttonPanel.add(btnGuncelle);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        loadSinavData();

        // Silme işlemi
        btnSil.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = sinavTable.getSelectedRow();
                if (selectedRow != -1) {
                    int sinavId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                    silSinav(sinavId);
                    loadSinavData();
                } else {
                    JOptionPane.showMessageDialog(null, "Lütfen silmek istediğiniz sınavı seçin.");
                }
            }
        });

        // Güncelleme işlemi
        btnGuncelle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = sinavTable.getSelectedRow();
                if (selectedRow != -1) {
                    int sinavId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                    String tarih = JOptionPane.showInputDialog("Yeni Tarihi Girin (YYYY-MM-DD):");
                    String baslangicSaati = JOptionPane.showInputDialog("Yeni Başlangıç Saatini Girin (HH:MM):");
                    String bitisSaati = JOptionPane.showInputDialog("Yeni Bitiş Saatini Girin (HH:MM):");
                    int ogrenciSayisi = Integer.parseInt(JOptionPane.showInputDialog("Öğrenci Sayısını Girin:"));

                    if (checkForConflicts(tarih, baslangicSaati, bitisSaati)) {
                        JOptionPane.showMessageDialog(null, "Bu saat diliminde bir sınav var, lütfen farklı bir zaman dilimi seçin.");
                    } else {
                        assignRoomsAndInvigilators(ogrenciSayisi, sinavId);
                        guncelleSinav(sinavId, tarih, baslangicSaati, bitisSaati);
                        loadSinavData();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Lütfen güncellemek istediğiniz sınavı seçin.");
                }
            }
        });

        setVisible(true);
    }

    private void loadSinavData() {
        String query = "SELECT s.id, d.name AS ders_adi, s.ogrenci_sayisi, s.date, s.start_time, s.end_time, " +
                       "s.salon_id_1, s.salon_id_2, s.salon_id_3, s.gozetmen_id_1, s.gozetmen_id_2, s.gozetmen_id_3 " +
                       "FROM otomasyon.sinav s JOIN otomasyon.dersler d ON s.ders_id = d.id";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            tableModel.setRowCount(0);

            while (resultSet.next()) {
                String salonAdlari = getSalonAdlari(resultSet);
                String gozetmenAdlari = getGozetmenAdlari(resultSet);

                tableModel.addRow(new Object[] {
                        resultSet.getInt("id"),
                        resultSet.getString("ders_adi"),
                        resultSet.getInt("ogrenci_sayisi"),
                        resultSet.getString("date"),
                        resultSet.getString("start_time"),
                        resultSet.getString("end_time"),
                        salonAdlari,
                        gozetmenAdlari
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getSalonAdlari(ResultSet resultSet) throws SQLException {
        StringBuilder salonAdlari = new StringBuilder();
        for (int i = 1; i <= 3; i++) {
            int salonId = resultSet.getInt("salon_id_" + i);
            if (salonId > 0) {
                salonAdlari.append("SALON").append(salonId).append(" ");
            }
        }
        return salonAdlari.toString().trim();
    }

    private String getGozetmenAdlari(ResultSet resultSet) throws SQLException {
        StringBuilder gozetmenAdlari = new StringBuilder();
        for (int i = 1; i <= 3; i++) {
            int gozetmenId = resultSet.getInt("gozetmen_id_" + i);
            if (gozetmenId > 0) {
                gozetmenAdlari.append("Gözetmen").append(gozetmenId).append(" ");
            }
        }
        return gozetmenAdlari.toString().trim();
    }

    private void silSinav(int sinavId) {
        String query = "DELETE FROM otomasyon.sinav WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, sinavId);
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Sınav başarıyla silindi.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void guncelleSinav(int sinavId, String tarih, String baslangicSaati, String bitisSaati) {
        String query = "UPDATE otomasyon.sinav SET date = ?, start_time = ?, end_time = ? WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, tarih);
            preparedStatement.setString(2, baslangicSaati);
            preparedStatement.setString(3, bitisSaati);
            preparedStatement.setInt(4, sinavId);
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Sınav başarıyla güncellendi.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean checkForConflicts(String date, String startTime, String endTime) {
        String query = "SELECT * FROM otomasyon.sinav WHERE date = ? AND (start_time < ? AND end_time > ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, date);
            preparedStatement.setString(2, endTime);
            preparedStatement.setString(3, startTime);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void assignRoomsAndInvigilators(int ogrenciSayisi, int sinavId) {
        if (ogrenciSayisi <= 0) {
            JOptionPane.showMessageDialog(null, "Öğrenci sayısı 0 olduğunda salon ve gözetmen ataması yapılmaz.");
            return;
        }

        int salonSayisi = (int) Math.ceil(ogrenciSayisi / 50.0);
        if (salonSayisi > 3) salonSayisi = 3;
        Set<Integer> assignedRooms = new HashSet<>();
        Set<Integer> assignedInvigilators = new HashSet<>();
        StringBuilder salonIds = new StringBuilder();
        StringBuilder gozetmenIds = new StringBuilder();

        Random rand = new Random();

        // Salonları veritabanından çekme
        List<Integer> salonList = getAvailableRooms();

        // Salon sayısına göre salonları atama
        for (int i = 1; i <= salonSayisi; i++) {
            int salonId = salonList.get(rand.nextInt(salonList.size())); // Veritabanından çekilen salonlar arasından rastgele bir salon seç
            while (assignedRooms.contains(salonId)) {
                salonId = salonList.get(rand.nextInt(salonList.size())); // Tekrar aynı salon seçilmesin
            }
            assignedRooms.add(salonId);
            salonIds.append(salonId).append(" ");

            // Gözetmen ataması
            int gozetmenId = rand.nextInt(10) + 1;
            while (assignedInvigilators.contains(gozetmenId)) {
                gozetmenId = rand.nextInt(10) + 1; // Gözetmen atamasında da tekrarlama olmasın
            }
            assignedInvigilators.add(gozetmenId);
            gozetmenIds.append(gozetmenId).append(" ");
        }

        // Veritabanına güncelleme
        String updateQuery = "UPDATE otomasyon.sinav SET salon_id_1 = ?, salon_id_2 = ?, salon_id_3 = ?, " +
                             "gozetmen_id_1 = ?, gozetmen_id_2 = ?, gozetmen_id_3 = ? WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            String[] salonIdsArray = salonIds.toString().trim().split(" ");
            String[] gozetmenIdsArray = gozetmenIds.toString().trim().split(" ");

            for (int i = 0; i < salonSayisi; i++) {
                preparedStatement.setInt(i + 1, Integer.parseInt(salonIdsArray[i]));
                preparedStatement.setInt(i + 4, Integer.parseInt(gozetmenIdsArray[i]));
            }
            preparedStatement.setInt(7, sinavId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Salonları veritabanından çeken metod
    private List<Integer> getAvailableRooms() {
        List<Integer> salonList = new ArrayList<>();
        String query = "SELECT id FROM otomasyon.salon"; // Salonlar tablosundan salon ID'lerini alıyoruz
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                salonList.add(resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salonList;
    }

    public static void main(String[] args) {
        new SinavGoruntulemeGUI();
    }
}