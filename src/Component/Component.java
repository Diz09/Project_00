package Component;

import Connect.DBConnection;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;

public class Component extends javax.swing.JPanel {

    public Component() {
        initComponents();
        setOpaque(false);
        setIconInitMonth();
        setInitRankLayanan();
        setInitRankBarang();
        setInitNewTransaksi();
    }
    
    private void setIconInitAll(){
        Statement st = null;
        ResultSet rs = null;
        try {
            Connection con = DBConnection.getConnection();
            st = con.createStatement();
            //iconClient
            rs = st.executeQuery
                ("SELECT COUNT(*) AS client FROM tb_pelanggan");
            int client = rs.next() ? rs.getInt("client") : 0;
            txt_client.setText(Integer.toString(client));
            //iconTransaksi
            rs = st.executeQuery(
                    "SELECT SUM(total_rows) as transaksi FROM\n" +
                    "(SELECT COUNT(*) as total_rows FROM tb_transaksi_barang union all \n" +
                    "SELECT COUNT(*) as total_rows FROM tb_transaksi_gromming union all \n" +
                    "SELECT COUNT(*) as total_rows FROM tb_transaksi_penitipan) as total_counts;"
                );
            int transaksi = rs.next() ? rs.getInt("transaksi") : 0;
            txt_transaksi.setText((Integer.toString(transaksi)));
            //iconJumlahPendapatan
            rs = st.executeQuery(
                    "SELECT\n" +
                    "COALESCE(SUM(tb_transaksi_barang.total), 0) + COALESCE(SUM(tb_transaksi_gromming.total), 0) + COALESCE(SUM(tb_transaksi_penitipan.total), 0) AS pendapatan\n" +
                    "FROM tb_transaksi_barang\n" +
                    "LEFT JOIN tb_transaksi_gromming \n" +
                    "ON tb_transaksi_barang.id_transaksi_barang = tb_transaksi_gromming.id_transaksi_gromming\n" +
                    "LEFT JOIN tb_transaksi_penitipan \n" +
                    "ON tb_transaksi_barang.id_transaksi_barang = tb_transaksi_penitipan.id_transaksi_penitipan"
            );
            int Pendapatan = rs.next() ? rs.getInt("pendapatan") : 0;
            DecimalFormat decimalFormat = new DecimalFormat("#,###");
            String totalFormatted = decimalFormat.format(Pendapatan);
            txt_pendapatan.setText(totalFormatted);
            //iconLaba
            rs = st.executeQuery(
                    "SELECT ((SUM(tb_barang.harga_jual) - SUM(tb_barang.harga_beli)) / SUM(tb_barang.harga_beli)) * 100 AS laba \n" +
                    "FROM tb_transaksi_barang\n" +
                    "JOIN tb_barang ON tb_transaksi_barang.id_barang = tb_barang.id_barang"
            );
            double laba = rs.next() ? rs.getDouble("laba") : 0;
            String labaString = String.format("%.1f", laba); //menampilkan angka desimal hanya dengan 1 digit di belakang koma
            txt_laba.setText(labaString + "%");
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void setIconInitDay() { 
        Statement st = null;
        ResultSet rs = null;
        try {
            Connection con = DBConnection.getConnection();
            st = con.createStatement();
            //iconClient
            rs = st.executeQuery
                ("SELECT COUNT(*) AS client_hari_ini FROM tb_pelanggan WHERE DATE(date) = CURDATE()");
            int clientHariIni = rs.next() ? rs.getInt("client_hari_ini") : 0;
            rs = st.executeQuery
                ("SELECT COUNT(*) AS client_kemarin FROM tb_pelanggan WHERE DATE(date) = DATE_SUB(CURDATE(), INTERVAL 1 DAY)");
            int clientKemarin = rs.next() ? rs.getInt("client_kemarin") : 0;
            if (clientHariIni > clientKemarin) {
                icon1.setVisible(true);
                icon2.setVisible(false);
            } else {
                icon1.setVisible(false);
                icon2.setVisible(true);
            }
            txt_client.setText(Integer.toString(clientHariIni));
            //iconTransaksi
            rs = st.executeQuery(
                    "SELECT SUM(total_rows) as transaksi_hariIni FROM\n" +
                    "(SELECT COUNT(*) as total_rows FROM tb_transaksi_barang WHERE tanggal_transaksi = CURDATE() union all \n" +
                    "SELECT COUNT(*) as total_rows FROM tb_transaksi_gromming WHERE tanggal_transaksi = CURDATE() union all \n" +
                    "SELECT COUNT(*) as total_rows FROM tb_transaksi_penitipan WHERE tanggal_transaksi = CURDATE()) as total_counts;"
                );
            int transaksiHariIni = rs.next() ? rs.getInt("transaksi_hariIni") : 0;
            rs = st.executeQuery(
                    "SELECT SUM(total_rows) as transaksi_kemarin FROM\n" +
                    "(SELECT COUNT(*) as total_rows FROM tb_transaksi_barang WHERE tanggal_transaksi = DATE_SUB(CURDATE(), INTERVAL 1 DAY) union all \n" +
                    "SELECT COUNT(*) as total_rows FROM tb_transaksi_gromming WHERE tanggal_transaksi = DATE_SUB(CURDATE(), INTERVAL 1 DAY) union all \n" +
                    "SELECT COUNT(*) as total_rows FROM tb_transaksi_penitipan WHERE tanggal_transaksi = DATE_SUB(CURDATE(), INTERVAL 1 DAY)) as total_counts;"
            );
            int transaksiKemarin = rs.next() ? rs.getInt("transaksi_kemarin") : 0;
            if (transaksiHariIni > transaksiKemarin) {
                icon3.setVisible(true);
                icon4.setVisible(false);
            } else {
                icon3.setVisible(false);
                icon4.setVisible(true);
            }
            txt_transaksi.setText((Integer.toString(transaksiHariIni)));
            //iconJumlahPendapatan
            rs = st.executeQuery(
                    "SELECT\n" +
                    "COALESCE(SUM(tb_transaksi_barang.total), 0) + COALESCE(SUM(tb_transaksi_gromming.total), 0) + COALESCE(SUM(tb_transaksi_penitipan.total), 0) AS pendapatan_hari_ini\n" +
                    "FROM tb_transaksi_barang\n" +
                    "LEFT JOIN tb_transaksi_gromming \n" +
                    "ON tb_transaksi_barang.id_transaksi_barang = tb_transaksi_gromming.id_transaksi_gromming\n" +
                    "LEFT JOIN tb_transaksi_penitipan \n" +
                    "ON tb_transaksi_barang.id_transaksi_barang = tb_transaksi_penitipan.id_transaksi_penitipan\n" +
                    "WHERE \n" +
                    "tb_transaksi_barang.tanggal_transaksi = CURDATE() OR tb_transaksi_gromming.tanggal_transaksi = CURDATE() OR tb_transaksi_penitipan.tanggal_transaksi = CURDATE()"
            );
            int PendapatanHariIni = rs.next() ? rs.getInt("pendapatan_hari_ini") : 0;
            rs = st.executeQuery(
                    "SELECT\n" +
                    "COALESCE(SUM(tb_transaksi_barang.total), 0) + COALESCE(SUM(tb_transaksi_gromming.total), 0) + COALESCE(SUM(tb_transaksi_penitipan.total), 0) AS pendapatan_kemarin\n" +
                    "FROM tb_transaksi_barang\n" +
                    "LEFT JOIN tb_transaksi_gromming \n" +
                    "ON tb_transaksi_barang.id_transaksi_barang = tb_transaksi_gromming.id_transaksi_gromming\n" +
                    "LEFT JOIN tb_transaksi_penitipan \n" +
                    "ON tb_transaksi_barang.id_transaksi_barang = tb_transaksi_penitipan.id_transaksi_penitipan\n" +
                    "WHERE \n" +
                    "tb_transaksi_barang.tanggal_transaksi = DATE_SUB(CURDATE(), INTERVAL 1 DAY) OR tb_transaksi_gromming.tanggal_transaksi = DATE_SUB(CURDATE(), INTERVAL 1 DAY) OR tb_transaksi_penitipan.tanggal_transaksi = DATE_SUB(CURDATE(), INTERVAL 1 DAY)"
            );
            int PendapatanKemarin = rs.next() ? rs.getInt("pendapatan_kemarin") : 0;
            if (PendapatanHariIni > PendapatanKemarin) {
                icon5.setVisible(true);
                icon6.setVisible(false);
            } else {
                icon5.setVisible(false);
                icon6.setVisible(true);
            }
            DecimalFormat decimalFormat = new DecimalFormat("#,###");
            String totalFormatted = decimalFormat.format(PendapatanHariIni);
            txt_pendapatan.setText(totalFormatted);
            //iconLaba
            rs = st.executeQuery(
                    "SELECT ((SUM(tb_barang.harga_jual) - SUM(tb_barang.harga_beli)) / SUM(tb_barang.harga_beli)) * 100 AS laba_hari_ini \n" +
                    "FROM tb_transaksi_barang\n" +
                    "JOIN tb_barang ON tb_transaksi_barang.id_barang = tb_barang.id_barang\n" +
                    "WHERE DATE(tb_transaksi_barang.tanggal_transaksi) = CURDATE();"
            );
            double LabaHariIni = rs.next() ? rs.getDouble("laba_hari_ini") : 0;
            rs = st.executeQuery(
                    "SELECT ((SUM(tb_barang.harga_jual) - SUM(tb_barang.harga_beli)) / SUM(tb_barang.harga_beli)) * 100 AS laba_kemarin \n" +
                    "FROM tb_transaksi_barang\n" +
                    "JOIN tb_barang ON tb_transaksi_barang.id_barang = tb_barang.id_barang\n" +
                    "WHERE DATE(tb_transaksi_barang.tanggal_transaksi) = DATE_SUB(CURDATE(), INTERVAL 1 DAY);"
            );
            double LabaKemarin = rs.next() ? rs.getDouble("laba_kemarin") : 0;
            if (LabaHariIni > LabaKemarin){
                icon7.setVisible(true);
                icon8.setVisible(false);
            }else{
                icon7.setVisible(false);
                icon8.setVisible(true);
            }
            String labaString = String.format("%.1f", LabaHariIni); //menampilkan angka desimal hanya dengan 1 digit di belakang koma
            txt_laba.setText(labaString + "%");
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void setIconInitWeek(){
        Statement st = null;
            ResultSet rs = null;
            try {
                Connection con = DBConnection.getConnection();
                st = con.createStatement();
                //client
                rs = st.executeQuery(
                        "SELECT COUNT(*) AS client_minggu_ini FROM tb_pelanggan WHERE date >= DATE_SUB(CURDATE(), INTERVAL 6 DAY)"
                );
                int clientMingguIni = rs.next() ? rs.getInt("client_minggu_ini") : 0;
                rs = st.executeQuery(
                        "SELECT COUNT(*) AS client_last_week FROM tb_pelanggan "
                        + "WHERE DATE(date) > DATE_SUB(CURDATE(), INTERVAL 2 WEEK) AND DATE(date) <= DATE_SUB(CURDATE(), INTERVAL 1 WEEK)"
                );
                int clientMingguKemarin = rs.next() ? rs.getInt("client_last_week") : 0;
                if (clientMingguIni > clientMingguKemarin) {
                    icon1.setVisible(true);
                    icon2.setVisible(false);
                } else {
                    icon1.setVisible(false);
                    icon2.setVisible(true);
                }
                txt_client.setText(Integer.toString(clientMingguIni));
                //transaski
                rs = st.executeQuery(
                        "SELECT SUM(total_rows) as transaksi_minggu_ini FROM ("
                        + "SELECT COUNT(*) as total_rows FROM tb_transaksi_barang WHERE DATE(tanggal_transaksi) >= DATE_SUB(CURDATE(), INTERVAL 6 DAY) union all "
                        + "SELECT COUNT(*) as total_rows FROM tb_transaksi_gromming WHERE DATE(tanggal_transaksi) >= DATE_SUB(CURDATE(), INTERVAL 6 DAY) union all "
                        + "SELECT COUNT(*) as total_rows FROM tb_transaksi_penitipan WHERE DATE(tanggal_transaksi) >= DATE_SUB(CURDATE(), INTERVAL 6 DAY)) "
                        + "AS total_counts"
                );
                int transaksiMingguIni = rs.next() ? rs.getInt("transaksi_minggu_ini") : 0;
                rs = st.executeQuery(
                        "SELECT SUM(total_rows) as transaksi_last_week FROM("
                        + "SELECT COUNT(*) as total_rows FROM tb_transaksi_barang "
                            + "WHERE DATE(tanggal_transaksi) > DATE_SUB(CURDATE(), INTERVAL 2 WEEK) AND DATE(tanggal_transaksi) <= DATE_SUB(CURDATE(), INTERVAL 1 WEEK) UNION ALL "
                        + "SELECT COUNT(*) as total_rows FROM tb_transaksi_gromming "
                            + "WHERE DATE(tanggal_transaksi) > DATE_SUB(CURDATE(), INTERVAL 2 WEEK) AND DATE(tanggal_transaksi) <= DATE_SUB(CURDATE(), INTERVAL 1 WEEK) UNION ALL "
                        + "SELECT COUNT(*) as total_rows FROM tb_transaksi_penitipan "
                            + "WHERE DATE(tanggal_transaksi) > DATE_SUB(CURDATE(), INTERVAL 2 WEEK) AND DATE(tanggal_transaksi) <= DATE_SUB(CURDATE(), INTERVAL 1 WEEK)) "
                        + "as total_count"
                );
                int transaksiMingguKemarin = rs.next() ? rs.getInt("transaksi_last_week") : 0;
                if (transaksiMingguIni > transaksiMingguKemarin) {
                    icon3.setVisible(true);
                    icon4.setVisible(false);
                } else {
                    icon3.setVisible(false);
                    icon4.setVisible(true);
                }
                txt_transaksi.setText((Integer.toString(transaksiMingguIni)));
                //pendapatan
                rs = st.executeQuery(
                        "SELECT \n" +
                        "COALESCE(SUM(tb_transaksi_barang.total), 0) + COALESCE(SUM(tb_transaksi_gromming.total), 0) + COALESCE(SUM(tb_transaksi_penitipan.total), 0) AS pendapatan_minggu_ini \n" +
                        "FROM tb_transaksi_barang \n" +
                        "LEFT JOIN tb_transaksi_gromming \n" +
                        "ON tb_transaksi_barang.id_transaksi_barang = tb_transaksi_gromming.id_transaksi_gromming \n" +
                        "LEFT JOIN tb_transaksi_penitipan \n" +
                        "ON tb_transaksi_barang.id_transaksi_barang = tb_transaksi_penitipan.id_transaksi_penitipan \n" +
                        "WHERE \n" +
                        "tb_transaksi_barang.tanggal_transaksi >= DATE_SUB(CURDATE(), INTERVAL 6 DAY) \n" +
                        "OR \n" +
                        "tb_transaksi_gromming.tanggal_transaksi >= DATE_SUB(CURDATE(), INTERVAL 6 DAY) \n" +
                        "OR \n" +
                        "tb_transaksi_penitipan.tanggal_transaksi >= DATE_SUB(CURDATE(), INTERVAL 6 DAY);"
                );
                int PendapatanMingguIni = rs.next() ? rs.getInt("pendapatan_minggu_ini") : 0;
                rs = st.executeQuery(
                        "SELECT \n" +
                        "COALESCE(SUM(tb_transaksi_barang.total), 0) + COALESCE(SUM(tb_transaksi_gromming.total), 0) + COALESCE(SUM(tb_transaksi_penitipan.total), 0) AS pendapatan_last_week \n" +
                        "FROM tb_transaksi_barang \n" +
                        "LEFT JOIN tb_transaksi_gromming \n" +
                        "ON tb_transaksi_barang.id_transaksi_barang = tb_transaksi_gromming.id_transaksi_gromming \n" +
                        "LEFT JOIN tb_transaksi_penitipan \n" +
                        "ON tb_transaksi_barang.id_transaksi_barang = tb_transaksi_penitipan.id_transaksi_penitipan \n" +
                        "WHERE \n" +
                        "(tb_transaksi_barang.tanggal_transaksi > DATE_SUB(CURDATE(), INTERVAL 2 WEEK) AND tb_transaksi_barang.tanggal_transaksi = DATE_SUB(CURDATE(), INTERVAL 1 WEEK)) \n" +
                        "OR \n" +
                        "(tb_transaksi_gromming.tanggal_transaksi > DATE_SUB(CURDATE(), INTERVAL 2 WEEK) AND tb_transaksi_gromming.tanggal_transaksi = DATE_SUB(CURDATE(), INTERVAL 1 WEEK)) \n" +
                        "OR \n" +
                        "(tb_transaksi_penitipan.tanggal_transaksi > DATE_SUB(CURDATE(), INTERVAL 2 WEEK) AND tb_transaksi_penitipan.tanggal_transaksi = DATE_SUB(CURDATE(), INTERVAL 1 WEEK))"
                );
                int PendapatanMingguKemarin = rs.next() ? rs.getInt("pendapatan_last_week") : 0;
                if (PendapatanMingguIni > PendapatanMingguKemarin) {
                    icon5.setVisible(true);
                    icon6.setVisible(false);
                } else {
                    icon5.setVisible(false);
                    icon6.setVisible(true);
                }
                DecimalFormat decimalFormat = new DecimalFormat("#,###");
                String totalFormatted = decimalFormat.format(PendapatanMingguIni);
                txt_pendapatan.setText(totalFormatted);
                //laba
                rs = st.executeQuery(
                        "SELECT ((SUM(tb_barang.harga_jual) - SUM(tb_barang.harga_beli)) / SUM(tb_barang.harga_beli)) * 100 AS laba_minggu_ini \n" +
                        "FROM tb_transaksi_barang\n" +
                        "JOIN tb_barang ON tb_transaksi_barang.id_barang = tb_barang.id_barang\n" +
                        "WHERE DATE(tb_transaksi_barang.tanggal_transaksi) >= DATE_SUB(CURDATE(), INTERVAL 6 DAY)"
                );
                double LabaMingguIni = rs.next() ? rs.getDouble("laba_minggu_ini") : 0;
                rs = st.executeQuery(
                        "SELECT ((SUM(tb_barang.harga_jual) - SUM(tb_barang.harga_beli)) / SUM(tb_barang.harga_beli)) * 100 AS laba_last_week \n" +
                        "FROM tb_transaksi_barang\n" +
                        "JOIN tb_barang ON tb_transaksi_barang.id_barang = tb_barang.id_barang\n" +
                        "WHERE DATE(tb_transaksi_barang.tanggal_transaksi) > DATE_SUB(CURDATE(), INTERVAL 2 WEEK) AND DATE(tanggal_transaksi) <= DATE_SUB(CURDATE(), INTERVAL 1 WEEK)"
                );
                double LabaMingguKemarin = rs.next() ? rs.getDouble("laba_last_week") : 0;
                if (LabaMingguIni > LabaMingguKemarin){
                    icon7.setVisible(true);
                    icon8.setVisible(false);
                }else{
                    icon7.setVisible(false);
                    icon8.setVisible(true);
                }
                String labaString = String.format("%.1f", LabaMingguIni);
                txt_laba.setText(labaString + "%");
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    
    private void setIconInitMonth(){
        Statement st = null;
            ResultSet rs = null;
            try {
                Connection con = DBConnection.getConnection();
                st = con.createStatement();
                //client
                rs = st.executeQuery(
                        "SELECT COUNT(*) AS client_bulan_ini FROM tb_pelanggan "
                        + "WHERE YEAR(date) = YEAR(CURDATE()) AND MONTH(date) = MONTH(CURDATE())"
                );
                int clientBulanIni = rs.next() ? rs.getInt("client_bulan_ini") : 0;
                rs = st.executeQuery(
                        "SELECT COUNT(*) AS client_last_month FROM tb_pelanggan "
                        + "WHERE YEAR(date) = YEAR(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)) AND MONTH(date) = MONTH(DATE_SUB(CURDATE(), INTERVAL 1 MONTH))"
                );
                int clientBulanKemarin = rs.next() ? rs.getInt("client_last_month") : 0;
                if (clientBulanIni > clientBulanKemarin) {
                    icon1.setVisible(true);
                    icon2.setVisible(false);
                } else {
                    icon1.setVisible(false);
                    icon2.setVisible(true);
                }
                txt_client.setText(Integer.toString(clientBulanIni));
                
                //transaski
                rs = st.executeQuery(
                        "SELECT SUM(total_rows) as transaksi_bulan_ini FROM ("
                        + "SELECT COUNT(*) as total_rows FROM tb_transaksi_barang "
                            + "WHERE YEAR(tanggal_transaksi) = YEAR(CURDATE()) AND MONTH(tanggal_transaksi) = MONTH(CURDATE()) union all "
                        + "SELECT COUNT(*) as total_rows FROM tb_transaksi_gromming "
                            + "WHERE YEAR(tanggal_transaksi) = YEAR(CURDATE()) AND MONTH(tanggal_transaksi) = MONTH(CURDATE()) union all "
                        + "SELECT COUNT(*) as total_rows FROM tb_transaksi_penitipan "
                            + "WHERE YEAR(tanggal_transaksi) = YEAR(CURDATE()) AND MONTH(tanggal_transaksi) = MONTH(CURDATE())) "
                        + "AS total_counts"
                );
                int transaksiBulanIni = rs.next() ? rs.getInt("transaksi_bulan_ini") : 0;
                rs = st.executeQuery(
                        "SELECT SUM(total_rows) as transaksi_last_month FROM ("
                        + "SELECT COUNT(*) as total_rows FROM tb_transaksi_barang "
                            + "WHERE YEAR(tanggal_transaksi) = YEAR(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)) AND MONTH(tanggal_transaksi) = MONTH(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)) union all "
                        + "SELECT COUNT(*) as total_rows FROM tb_transaksi_gromming "
                            + "WHERE YEAR(tanggal_transaksi) = YEAR(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)) AND MONTH(tanggal_transaksi) = MONTH(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)) union all "
                        + "SELECT COUNT(*) as total_rows FROM tb_transaksi_penitipan "
                            + "WHERE YEAR(tanggal_transaksi) = YEAR(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)) AND MONTH(tanggal_transaksi) = MONTH(DATE_SUB(CURDATE(), INTERVAL 1 MONTH))) "
                        + "AS total_counts"
                );
                int transaksiBulanKemarin = rs.next() ? rs.getInt("transaksi_last_month") : 0;
                if (transaksiBulanIni > transaksiBulanKemarin) {
                    icon3.setVisible(true);
                    icon4.setVisible(false);
                } else {
                    icon3.setVisible(false);
                    icon4.setVisible(true);
                }
                txt_transaksi.setText((Integer.toString(transaksiBulanIni)));
                //pendapatan
                rs = st.executeQuery(
                        "SELECT \n" +
                        "COALESCE(SUM(tb_transaksi_barang.total), 0) + COALESCE(SUM(tb_transaksi_gromming.total), 0) + COALESCE(SUM(tb_transaksi_penitipan.total), 0) AS pendapatan_bulan_ini \n" +
                        "FROM tb_transaksi_barang \n" +
                        "LEFT JOIN tb_transaksi_gromming \n" +
                        "ON tb_transaksi_barang.id_transaksi_barang = tb_transaksi_gromming.id_transaksi_gromming \n" +
                        "LEFT JOIN tb_transaksi_penitipan \n" +
                        "ON tb_transaksi_barang.id_transaksi_barang = tb_transaksi_penitipan.id_transaksi_penitipan \n" +
                        "WHERE \n" +
                        "(YEAR(tb_transaksi_barang.tanggal_transaksi) = YEAR(CURDATE()) AND MONTH(tb_transaksi_barang.tanggal_transaksi) = MONTH(CURDATE()))\n" +
                        "OR \n" +
                        "(YEAR(tb_transaksi_gromming.tanggal_transaksi) = YEAR(CURDATE()) AND MONTH(tb_transaksi_gromming.tanggal_transaksi) = MONTH(CURDATE()))\n" +
                        "OR \n" +
                        "(YEAR(tb_transaksi_penitipan.tanggal_transaksi) = YEAR(CURDATE()) AND MONTH(tb_transaksi_penitipan.tanggal_transaksi) = MONTH(CURDATE()))"
                );
                int PendapatanBulanIni = rs.next() ? rs.getInt("pendapatan_bulan_ini") : 0;
                rs = st.executeQuery(
                        "SELECT \n" +
                        "COALESCE(SUM(tb_transaksi_barang.total), 0) + COALESCE(SUM(tb_transaksi_gromming.total), 0) + COALESCE(SUM(tb_transaksi_penitipan.total), 0) AS pendapatan_last_month\n" +
                        "FROM tb_transaksi_barang \n" +
                        "LEFT JOIN tb_transaksi_gromming \n" +
                        "ON tb_transaksi_barang.id_transaksi_barang = tb_transaksi_gromming.id_transaksi_gromming \n" +
                        "LEFT JOIN tb_transaksi_penitipan \n" +
                        "ON tb_transaksi_barang.id_transaksi_barang = tb_transaksi_penitipan.id_transaksi_penitipan \n" +
                        "WHERE \n" +
                        "(YEAR(tb_transaksi_barang.tanggal_transaksi) = YEAR(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)) AND MONTH(tb_transaksi_barang.tanggal_transaksi) = MONTH(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)))\n" +
                        "OR \n" +
                        "(YEAR(tb_transaksi_gromming.tanggal_transaksi) = YEAR(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)) AND MONTH(tb_transaksi_gromming.tanggal_transaksi) = MONTH(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)))\n" +
                        "OR \n" +
                        "(YEAR(tb_transaksi_penitipan.tanggal_transaksi) = YEAR(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)) AND MONTH(tb_transaksi_penitipan.tanggal_transaksi) = MONTH(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)))"
                );
                int PendapatanBulanKemarin = rs.next() ? rs.getInt("pendapatan_last_month") : 0;
                if (PendapatanBulanIni > PendapatanBulanKemarin) {
                    icon5.setVisible(true);
                    icon6.setVisible(false);
                } else {
                    icon5.setVisible(false);
                    icon6.setVisible(true);
                }
                DecimalFormat decimalFormat = new DecimalFormat("#,###");
                String totalFormatted = decimalFormat.format(PendapatanBulanIni);
                txt_pendapatan.setText(totalFormatted);
                //laba
                rs = st.executeQuery(
                        "SELECT ((SUM(tb_barang.harga_jual) - SUM(tb_barang.harga_beli)) / SUM(tb_barang.harga_beli)) * 100 AS laba_bulan_ini\n" +
                        "FROM tb_transaksi_barang\n" +
                        "JOIN tb_barang ON tb_transaksi_barang.id_barang = tb_barang.id_barang\n" +
                        "WHERE YEAR(tb_transaksi_barang.tanggal_transaksi) = YEAR(CURDATE()) AND MONTH(tb_transaksi_barang.tanggal_transaksi) = MONTH(CURDATE())"
                );
                double LabaBulanIni = rs.next() ? rs.getDouble("laba_bulan_ini") : 0;
                rs = st.executeQuery(
                        "SELECT ((SUM(tb_barang.harga_jual) - SUM(tb_barang.harga_beli)) / SUM(tb_barang.harga_beli)) * 100 AS laba_last_month \n" +
                        "FROM tb_transaksi_barang\n" +
                        "JOIN tb_barang ON tb_transaksi_barang.id_barang = tb_barang.id_barang\n" +
                        "WHERE YEAR(tb_transaksi_barang.tanggal_transaksi) = YEAR(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)) AND MONTH(tb_transaksi_barang.tanggal_transaksi) = MONTH(DATE_SUB(CURDATE(), INTERVAL 1 MONTH))"
                );
                double LabaBulanKemarin = rs.next() ? rs.getDouble("laba_last_month") : 0;
                if (LabaBulanIni > LabaBulanKemarin){
                    icon7.setVisible(true);
                    icon8.setVisible(false);
                }else{
                    icon7.setVisible(false);
                    icon8.setVisible(true);
                }
                String labaString = String.format("%.1f", LabaBulanIni); //menampilkan angka desimal hanya dengan 1 digit di belakang koma
                txt_laba.setText(labaString + "%");
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    
    private void setIconInitYear(){
        Statement st = null;
        ResultSet rs = null;
        try {
            Connection con = DBConnection.getConnection();
            st = con.createStatement();
            //client
            rs = st.executeQuery(
                    "SELECT COUNT(*) AS client_tahun_ini FROM tb_pelanggan WHERE YEAR(date) = YEAR(CURDATE())"
            );
            int clientTahunIni = rs.next() ? rs.getInt("client_tahun_ini") : 0;
            rs = st.executeQuery(
                    "SELECT COUNT(*) AS client_last_year FROM tb_pelanggan WHERE YEAR(date) = YEAR(DATE_SUB(CURDATE(), INTERVAL 1 YEAR))"
            );
            int clientTahunKemarin = rs.next() ? rs.getInt("client_last_year") : 0;
            if (clientTahunIni > clientTahunKemarin) {
                icon1.setVisible(true);
                icon2.setVisible(false);
            } else {
                icon1.setVisible(false);
                icon2.setVisible(true);
            }
            txt_client.setText(Integer.toString(clientTahunIni));

            //transaski
            rs = st.executeQuery(
                    "SELECT SUM(total_rows) as transaksi_tahun_ini FROM (\n" +
                    "SELECT COUNT(*) as total_rows FROM tb_transaksi_barang \n" +
                    "WHERE YEAR(tanggal_transaksi) = YEAR(CURDATE())\n" +
                    "union all SELECT COUNT(*) as total_rows FROM tb_transaksi_gromming \n" +
                    "WHERE YEAR(tanggal_transaksi) = YEAR(CURDATE())\n" +
                    "union all SELECT COUNT(*) as total_rows FROM tb_transaksi_penitipan \n" +
                    "WHERE YEAR(tanggal_transaksi) = YEAR(CURDATE())\n" +
                    ") AS total_counts"
            );
            int transaksiTahunIni = rs.next() ? rs.getInt("transaksi_tahun_ini") : 0;
            rs = st.executeQuery(
                    "SELECT SUM(total_rows) as transaksi_last_year FROM (\n" +
                    "SELECT COUNT(*) as total_rows FROM tb_transaksi_barang \n" +
                    "WHERE YEAR(tanggal_transaksi) = YEAR(DATE_SUB(CURDATE(), INTERVAL 1 YEAR))\n" +
                    "union all SELECT COUNT(*) as total_rows FROM tb_transaksi_gromming \n" +
                    "WHERE YEAR(tanggal_transaksi) = YEAR(DATE_SUB(CURDATE(), INTERVAL 1 YEAR))\n" +
                    "union all SELECT COUNT(*) as total_rows FROM tb_transaksi_penitipan \n" +
                    "WHERE YEAR(tanggal_transaksi) = YEAR(DATE_SUB(CURDATE(), INTERVAL 1 YEAR))\n" +
                    ") AS total_counts"
            );
            int transaksiTahunKemarin = rs.next() ? rs.getInt("transaksi_last_year") : 0;
            if (transaksiTahunIni > transaksiTahunKemarin) {
                icon3.setVisible(true);
                icon4.setVisible(false);
            } else {
                icon3.setVisible(false);
                icon4.setVisible(true);
            }
            txt_transaksi.setText((Integer.toString(transaksiTahunIni)));
            //pendapatan
            rs = st.executeQuery(
                    "SELECT \n" +
                    "COALESCE(SUM(tb_transaksi_barang.total), 0) + COALESCE(SUM(tb_transaksi_gromming.total), 0) + COALESCE(SUM(tb_transaksi_penitipan.total), 0) AS pendapatan_tahun_ini \n" +
                    "FROM tb_transaksi_barang \n" +
                    "LEFT JOIN tb_transaksi_gromming \n" +
                    "ON tb_transaksi_barang.id_transaksi_barang = tb_transaksi_gromming.id_transaksi_gromming \n" +
                    "LEFT JOIN tb_transaksi_penitipan \n" +
                    "ON tb_transaksi_barang.id_transaksi_barang = tb_transaksi_penitipan.id_transaksi_penitipan \n" +
                    "WHERE \n" +
                    "(YEAR(tb_transaksi_barang.tanggal_transaksi) = YEAR(CURDATE()))\n" +
                    "OR \n" +
                    "(YEAR(tb_transaksi_gromming.tanggal_transaksi) = YEAR(CURDATE()))\n" +
                    "OR \n" +
                    "(YEAR(tb_transaksi_penitipan.tanggal_transaksi) = YEAR(CURDATE()))"
            );
            int PendapatanTahunIni = rs.next() ? rs.getInt("pendapatan_tahun_ini") : 0;
            rs = st.executeQuery(
                    "SELECT \n" +
                    "COALESCE(SUM(tb_transaksi_barang.total), 0) + COALESCE(SUM(tb_transaksi_gromming.total), 0) + COALESCE(SUM(tb_transaksi_penitipan.total), 0) AS pendapatan_last_year \n" +
                    "FROM tb_transaksi_barang \n" +
                    "LEFT JOIN tb_transaksi_gromming \n" +
                    "ON tb_transaksi_barang.id_transaksi_barang = tb_transaksi_gromming.id_transaksi_gromming \n" +
                    "LEFT JOIN tb_transaksi_penitipan \n" +
                    "ON tb_transaksi_barang.id_transaksi_barang = tb_transaksi_penitipan.id_transaksi_penitipan \n" +
                    "WHERE \n" +
                    "(YEAR(tb_transaksi_barang.tanggal_transaksi) = YEAR(DATE_SUB(CURDATE(), INTERVAL 1 YEAR)))\n" +
                    "OR \n" +
                    "(YEAR(tb_transaksi_gromming.tanggal_transaksi) = YEAR(DATE_SUB(CURDATE(), INTERVAL 1 YEAR)))\n" +
                    "OR \n" +
                    "(YEAR(tb_transaksi_penitipan.tanggal_transaksi) = YEAR(DATE_SUB(CURDATE(), INTERVAL 1 YEAR)))"
            );
            int PendapatanTahunKemarin = rs.next() ? rs.getInt("pendapatan_last_year") : 0;
            if (PendapatanTahunIni > PendapatanTahunKemarin) {
                icon5.setVisible(true);
                icon6.setVisible(false);
            } else {
                icon5.setVisible(false);
                icon6.setVisible(true);
            }
            DecimalFormat decimalFormat = new DecimalFormat("#,###");
            String totalFormatted = decimalFormat.format(PendapatanTahunIni);
            txt_pendapatan.setText(totalFormatted);
            //laba
            rs = st.executeQuery(
                    "SELECT ((SUM(tb_barang.harga_jual) - SUM(tb_barang.harga_beli)) / SUM(tb_barang.harga_beli)) * 100 AS laba_tahun_ini \n" +
                    "FROM tb_transaksi_barang\n" +
                    "JOIN tb_barang ON tb_transaksi_barang.id_barang = tb_barang.id_barang\n" +
                    "WHERE YEAR(tb_transaksi_barang.tanggal_transaksi) = YEAR(CURDATE())"
            );
            double LabaTahunIni = rs.next() ? rs.getDouble("laba_tahun_ini") : 0;
            rs = st.executeQuery(
                    "SELECT ((SUM(tb_barang.harga_jual) - SUM(tb_barang.harga_beli)) / SUM(tb_barang.harga_beli)) * 100 AS laba_last_year \n" +
                    "FROM tb_transaksi_barang\n" +
                    "JOIN tb_barang ON tb_transaksi_barang.id_barang = tb_barang.id_barang\n" +
                    "WHERE YEAR(tb_transaksi_barang.tanggal_transaksi) = YEAR(DATE_SUB(CURDATE(), INTERVAL 1 YEAR))"
            );
            double LabaTahunKemarin = rs.next() ? rs.getDouble("laba_last_year") : 0;
            if (LabaTahunIni > LabaTahunKemarin){
                icon7.setVisible(true);
                icon8.setVisible(false);
            }else{
                icon7.setVisible(false);
                icon8.setVisible(true);
            }
            String labaString = String.format("%.1f", LabaTahunIni); //menampilkan angka desimal hanya dengan 1 digit di belakang koma
            txt_laba.setText(labaString + "%");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setInitRankLayanan(){
        Statement st = null;
        ResultSet rs = null;
        
        try{
            Connection con = DBConnection.getConnection();
            st = con.createStatement();
            //rankLayanan
            for(int i = 0; i < 3; i++){
                rs = st.executeQuery(
                        "SELECT 'gromming' AS sumber, tb_gromming.jenis_gromming AS nama, tb_transaksi_gromming.id_gromming AS id, "
                                + "COUNT(tb_transaksi_gromming.id_transaksi_gromming) AS jml\n" +
                        "FROM tb_transaksi_gromming INNER JOIN tb_gromming \n" +
                        "ON tb_transaksi_gromming.id_gromming = tb_gromming.id_gromming\n" +
                        "GROUP BY tb_transaksi_gromming.id_gromming\n" +
                        "UNION ALL\n" +
                        "SELECT 'penitipan' AS sumber, tb_penitipan.jenis_penitipan AS nama, tb_transaksi_penitipan.id_penitipan AS id, "
                                + "COUNT(tb_transaksi_penitipan.id_transaksi_penitipan) AS jml\n" +
                        "FROM tb_transaksi_penitipan INNER JOIN tb_penitipan \n" +
                        "ON tb_transaksi_penitipan.id_penitipan = tb_penitipan.id_penitipan\n" +
                        "GROUP BY tb_transaksi_penitipan.id_penitipan\n" +
                        "ORDER BY jml DESC\n" +
                        "LIMIT "+ i +",1;"
                );
                
                if(rs.next()){
                    String namaLayanan = rs.getString("sumber");
                    String descLayanan = rs.getString("nama");
                    
                    switch(i){
                        case 0:
                            titleRLayanan1.setText(namaLayanan);
                            descRLayanan1.setText(descLayanan);
                            break;
                        case 1:
                            titleRLayanan2.setText(namaLayanan);
                            descRLayanan2.setText(descLayanan);
                            break;
                        case 2:
                            titleRLayanan3.setText(namaLayanan);
                            descRLayanan3.setText(descLayanan);
                            break;
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void setInitRankBarang(){
        Statement st = null;
        ResultSet rs = null;
        
        try {
            Connection con = DBConnection.getConnection();
            st = con.createStatement();
            
            //set data rank barang
            for(int i = 0; i < 3; i++){
                rs = st.executeQuery(
                    "SELECT tb1.nama_barang, tb1.kategori," +
                    "SUM(tb2.jumlah) AS total_beli \n" +
                    "FROM tb_barang AS tb1 INNER JOIN tb_transaksi_barang AS tb2\n" +
                    "ON tb1.id_barang = tb2.id_barang\n" +
                    "GROUP BY nama_barang \n" +
                    "ORDER BY total_beli DESC LIMIT " + i + ",1");

                if(rs.next()){
                    String namaBarang = rs.getString("nama_barang");
                    String descBarang = rs.getString("kategori");
                    switch(i){
                        case 0:
                            titleRBarang1.setText(namaBarang);
                            descRBarang1.setText(descBarang);
                            break;
                        case 1:
                            titleRBarang2.setText(namaBarang);
                            descRBarang2.setText(descBarang);
                            break;
                        case 2:
                            titleRBarang3.setText(namaBarang);
                            descRBarang3.setText(descBarang);
                            break;
                    }
                }
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void setInitNewTransaksi(){
        Statement st = null;
        ResultSet rs = null;
        
         try {
            Connection con = DBConnection.getConnection();
            st = con.createStatement();
            
            for(int i = 0; i < 6; i++){
                rs = st.executeQuery(
                    "SELECT 'Barang' AS sumber, tb_transaksi_barang.id_transaksi_barang AS id, tb_barang.nama_barang AS nama, tb_transaksi_barang.sub_total AS harga, tb_transaksi_barang.tanggal_transaksi AS tanggal_transaksi\n" +
                    "FROM tb_transaksi_barang INNER JOIN tb_barang \n" +
                    "ON tb_transaksi_barang.id_barang = tb_barang.id_barang\n" +
                    "UNION ALL\n" +
                    "SELECT 'Gromming' AS sumber, tb_transaksi_gromming.id_transaksi_gromming AS id, tb_gromming.jenis_gromming AS nama, tb_transaksi_gromming.total AS harga, tb_transaksi_gromming.tanggal_transaksi AS tanggal_transaksi\n" +
                    "FROM tb_transaksi_gromming INNER JOIN tb_gromming \n" +
                    "ON tb_transaksi_gromming.id_gromming = tb_gromming.id_gromming\n" +
                    "UNION ALL\n" +
                    "SELECT 'Penitipan' AS sumber, tb_transaksi_penitipan.id_transaksi_penitipan AS id, tb_penitipan.jenis_penitipan AS nama, tb_transaksi_penitipan.total AS harga, tb_transaksi_penitipan.tanggal_transaksi AS tanggal_transaksi\n" +
                    "FROM tb_transaksi_penitipan INNER JOIN tb_penitipan \n" +
                    "ON tb_transaksi_penitipan.id_penitipan = tb_penitipan.id_penitipan \n" +
                    "ORDER BY tanggal_transaksi DESC LIMIT "+ i +",1"
                );
                if (rs.next()) {
                    String sumb = rs.getString("sumber");
                    String nama = rs.getString("nama");
                    int pric = rs.getInt("harga");
                    
                    DecimalFormat decimalFormat = new DecimalFormat("#,###");
                    String formatted = decimalFormat.format(pric);
                    
                    switch(i){
                        case 0:
                            sumberNTrsn1.setText(sumb);
                            titleNTrsn1.setText(nama);
                            priceNTrsn1.setText("Rp "+ formatted);
                            break;
                        case 1:
                            sumberNTrsn2.setText(sumb);
                            titleNTrsn2.setText(nama);
                            priceNTrsn2.setText("Rp "+ formatted);
                            break;
                        case 2:
                            sumberNTrsn3.setText(sumb);
                            titleNTrsn3.setText(nama);
                            priceNTrsn3.setText("Rp "+ formatted);
                            break;
                        case 3:
                            sumberNTrsn4.setText(sumb);
                            titleNTrsn4.setText(nama);
                            priceNTrsn4.setText("Rp "+ formatted);
                            break;
                        case 4:
                            sumberNTrsn5.setText(sumb);
                            titleNTrsn5.setText(nama);
                            priceNTrsn5.setText("Rp "+ formatted);
                            break;
                        case 5:
                            sumberNTrsn6.setText(sumb);
                            titleNTrsn6.setText(nama);
                            priceNTrsn6.setText("Rp "+ formatted);
                            break;
                    }
                }
            }
         }  catch(Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        combobox1 = new swing.comboBox.Combobox();
        title1 = new javax.swing.JLabel();
        title2 = new javax.swing.JLabel();
        title3 = new javax.swing.JLabel();
        title4 = new javax.swing.JLabel();
        toTransaksi = new javax.swing.JLabel();
        txt_client = new javax.swing.JLabel();
        txt_transaksi = new javax.swing.JLabel();
        txt_pendapatan = new javax.swing.JLabel();
        txt_laba = new javax.swing.JLabel();
        icon1 = new javax.swing.JLabel();
        icon2 = new javax.swing.JLabel();
        icon3 = new javax.swing.JLabel();
        icon4 = new javax.swing.JLabel();
        icon5 = new javax.swing.JLabel();
        icon6 = new javax.swing.JLabel();
        icon7 = new javax.swing.JLabel();
        icon8 = new javax.swing.JLabel();
        titleRLayanan1 = new javax.swing.JLabel();
        titleRLayanan2 = new javax.swing.JLabel();
        titleRLayanan3 = new javax.swing.JLabel();
        descRLayanan1 = new javax.swing.JLabel();
        descRLayanan2 = new javax.swing.JLabel();
        descRLayanan3 = new javax.swing.JLabel();
        titleRBarang1 = new javax.swing.JLabel();
        titleRBarang2 = new javax.swing.JLabel();
        titleRBarang3 = new javax.swing.JLabel();
        descRBarang1 = new javax.swing.JLabel();
        descRBarang2 = new javax.swing.JLabel();
        descRBarang3 = new javax.swing.JLabel();
        sumberNTrsn1 = new javax.swing.JLabel();
        sumberNTrsn2 = new javax.swing.JLabel();
        sumberNTrsn3 = new javax.swing.JLabel();
        sumberNTrsn4 = new javax.swing.JLabel();
        sumberNTrsn5 = new javax.swing.JLabel();
        sumberNTrsn6 = new javax.swing.JLabel();
        titleNTrsn1 = new javax.swing.JLabel();
        titleNTrsn2 = new javax.swing.JLabel();
        titleNTrsn3 = new javax.swing.JLabel();
        titleNTrsn4 = new javax.swing.JLabel();
        titleNTrsn5 = new javax.swing.JLabel();
        titleNTrsn6 = new javax.swing.JLabel();
        priceNTrsn1 = new javax.swing.JLabel();
        priceNTrsn2 = new javax.swing.JLabel();
        priceNTrsn3 = new javax.swing.JLabel();
        priceNTrsn4 = new javax.swing.JLabel();
        priceNTrsn5 = new javax.swing.JLabel();
        priceNTrsn6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        borderHide = new javax.swing.JLabel();
        borderShow = new javax.swing.JLabel();

        setBackground(new java.awt.Color(235, 237, 241));
        setPreferredSize(new java.awt.Dimension(1920, 1080));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        combobox1.setBorder(null);
        combobox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All", "in Days", "in Weeks", "in Months", "in Years" }));
        combobox1.setSelectedIndex(3);
        combobox1.setFont(new java.awt.Font("Microsoft Tai Le", 0, 12)); // NOI18N
        combobox1.setLabeTaxt("");
        combobox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combobox1ActionPerformed(evt);
            }
        });
        add(combobox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1690, 120, 140, 30));

        title1.setFont(new java.awt.Font("Microsoft Tai Le", 1, 32)); // NOI18N
        title1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        title1.setText("Client Bulan Ini");
        add(title1, new org.netbeans.lib.awtextra.AbsoluteConstraints(478, 185, 290, 30));

        title2.setFont(new java.awt.Font("Microsoft Tai Le", 1, 32)); // NOI18N
        title2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        title2.setText("Transaksi");
        add(title2, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 185, 230, 30));

        title3.setFont(new java.awt.Font("Microsoft Tai Le", 1, 32)); // NOI18N
        title3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        title3.setText("Jumlah Pendapatan");
        add(title3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1060, 185, 390, 30));

        title4.setFont(new java.awt.Font("Microsoft Tai Le", 1, 32)); // NOI18N
        title4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        title4.setText("Presentase Untung");
        add(title4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1460, 185, 340, 30));
        add(toTransaksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(1710, 1006, 130, 20));

        txt_client.setFont(new java.awt.Font("Microsoft Tai Le", 1, 64)); // NOI18N
        txt_client.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txt_client.setText("300");
        add(txt_client, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 238, 120, 60));

        txt_transaksi.setFont(new java.awt.Font("Microsoft Tai Le", 1, 64)); // NOI18N
        txt_transaksi.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txt_transaksi.setText("3000");
        add(txt_transaksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 238, 150, 60));

        txt_pendapatan.setFont(new java.awt.Font("Microsoft Tai Le", 1, 64)); // NOI18N
        txt_pendapatan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txt_pendapatan.setText("9,000,000");
        add(txt_pendapatan, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 238, 290, 60));

        txt_laba.setFont(new java.awt.Font("Microsoft Tai Le", 1, 64)); // NOI18N
        txt_laba.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txt_laba.setText("90.0 %");
        add(txt_laba, new org.netbeans.lib.awtextra.AbsoluteConstraints(1570, 238, -1, 60));

        icon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/up.png"))); // NOI18N
        add(icon1, new org.netbeans.lib.awtextra.AbsoluteConstraints(525, 238, -1, -1));

        icon2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/down.png"))); // NOI18N
        add(icon2, new org.netbeans.lib.awtextra.AbsoluteConstraints(525, 238, -1, -1));

        icon3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/up.png"))); // NOI18N
        add(icon3, new org.netbeans.lib.awtextra.AbsoluteConstraints(811, 238, -1, -1));

        icon4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/down.png"))); // NOI18N
        add(icon4, new org.netbeans.lib.awtextra.AbsoluteConstraints(811, 238, -1, -1));

        icon5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/up.png"))); // NOI18N
        add(icon5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1070, 238, -1, -1));

        icon6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/down.png"))); // NOI18N
        add(icon6, new org.netbeans.lib.awtextra.AbsoluteConstraints(1070, 238, -1, -1));

        icon7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/up.png"))); // NOI18N
        add(icon7, new org.netbeans.lib.awtextra.AbsoluteConstraints(1500, 238, -1, -1));

        icon8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/down.png"))); // NOI18N
        add(icon8, new org.netbeans.lib.awtextra.AbsoluteConstraints(1500, 238, -1, -1));

        titleRLayanan1.setFont(new java.awt.Font("Microsoft Tai Le", 1, 22)); // NOI18N
        titleRLayanan1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleRLayanan1.setText("Gromming Sehat");
        add(titleRLayanan1, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 576, 230, 30));

        titleRLayanan2.setFont(new java.awt.Font("Microsoft Tai Le", 1, 22)); // NOI18N
        titleRLayanan2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleRLayanan2.setText("Gromming Kutu");
        add(titleRLayanan2, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 576, 240, 30));

        titleRLayanan3.setFont(new java.awt.Font("Microsoft Tai Le", 1, 22)); // NOI18N
        titleRLayanan3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleRLayanan3.setText("Klinik");
        add(titleRLayanan3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 576, 230, 30));

        descRLayanan1.setFont(new java.awt.Font("Microsoft Tai Le", 1, 16)); // NOI18N
        descRLayanan1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        descRLayanan1.setText("Gromming Kucing Sehat");
        descRLayanan1.setPreferredSize(new java.awt.Dimension(175, 16));
        add(descRLayanan1, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 608, 230, 20));

        descRLayanan2.setFont(new java.awt.Font("Microsoft Tai Le", 1, 16)); // NOI18N
        descRLayanan2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        descRLayanan2.setText("Gromming Kucing Sehat");
        descRLayanan2.setPreferredSize(new java.awt.Dimension(175, 16));
        add(descRLayanan2, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 608, 240, 20));

        descRLayanan3.setFont(new java.awt.Font("Microsoft Tai Le", 1, 16)); // NOI18N
        descRLayanan3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        descRLayanan3.setText("Gromming Kucing Sehat");
        descRLayanan3.setPreferredSize(new java.awt.Dimension(175, 16));
        add(descRLayanan3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 608, 230, 20));

        titleRBarang1.setFont(new java.awt.Font("Microsoft Tai Le", 1, 22)); // NOI18N
        titleRBarang1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleRBarang1.setText("Whiskas");
        add(titleRBarang1, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 870, 230, 30));

        titleRBarang2.setFont(new java.awt.Font("Microsoft Tai Le", 1, 22)); // NOI18N
        titleRBarang2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleRBarang2.setText("Susu Ultramilk");
        add(titleRBarang2, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 870, 240, 30));

        titleRBarang3.setFont(new java.awt.Font("Microsoft Tai Le", 1, 22)); // NOI18N
        titleRBarang3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleRBarang3.setText("Aksesoris");
        add(titleRBarang3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 870, 230, 30));

        descRBarang1.setFont(new java.awt.Font("Microsoft Tai Le", 1, 16)); // NOI18N
        descRBarang1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        descRBarang1.setText("Whiskas varian Sedang");
        descRBarang1.setPreferredSize(new java.awt.Dimension(175, 16));
        add(descRBarang1, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 900, 230, 20));

        descRBarang2.setFont(new java.awt.Font("Microsoft Tai Le", 1, 16)); // NOI18N
        descRBarang2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        descRBarang2.setText("Susu Ultramilk 30g");
        descRBarang2.setPreferredSize(new java.awt.Dimension(175, 16));
        add(descRBarang2, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 900, 240, 20));

        descRBarang3.setFont(new java.awt.Font("Microsoft Tai Le", 1, 16)); // NOI18N
        descRBarang3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        descRBarang3.setText("Hodie Kucing");
        descRBarang3.setPreferredSize(new java.awt.Dimension(175, 16));
        add(descRBarang3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 900, 230, 20));

        sumberNTrsn1.setFont(new java.awt.Font("Microsoft Tai Le", 1, 20)); // NOI18N
        sumberNTrsn1.setText("Gromming");
        add(sumberNTrsn1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1448, 458, 220, 30));

        sumberNTrsn2.setFont(new java.awt.Font("Microsoft Tai Le", 1, 20)); // NOI18N
        sumberNTrsn2.setText("Gromming");
        add(sumberNTrsn2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1448, 550, 220, 30));

        sumberNTrsn3.setFont(new java.awt.Font("Microsoft Tai Le", 1, 20)); // NOI18N
        sumberNTrsn3.setText("Gromming");
        add(sumberNTrsn3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1448, 645, 220, 30));

        sumberNTrsn4.setFont(new java.awt.Font("Microsoft Tai Le", 1, 20)); // NOI18N
        sumberNTrsn4.setText("Gromming");
        add(sumberNTrsn4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1448, 738, 220, 30));

        sumberNTrsn5.setFont(new java.awt.Font("Microsoft Tai Le", 1, 20)); // NOI18N
        sumberNTrsn5.setText("Gromming");
        add(sumberNTrsn5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1448, 830, 220, 30));

        sumberNTrsn6.setFont(new java.awt.Font("Microsoft Tai Le", 1, 20)); // NOI18N
        sumberNTrsn6.setText("Gromming");
        add(sumberNTrsn6, new org.netbeans.lib.awtextra.AbsoluteConstraints(1448, 924, 220, 30));

        titleNTrsn1.setFont(new java.awt.Font("Microsoft Tai Le", 1, 18)); // NOI18N
        titleNTrsn1.setText("Gromming Kutu");
        add(titleNTrsn1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1448, 488, 220, 20));

        titleNTrsn2.setFont(new java.awt.Font("Microsoft Tai Le", 1, 18)); // NOI18N
        titleNTrsn2.setText("Gromming Kutu");
        add(titleNTrsn2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1448, 580, 220, 20));

        titleNTrsn3.setFont(new java.awt.Font("Microsoft Tai Le", 1, 18)); // NOI18N
        titleNTrsn3.setText("Gromming Kutu");
        add(titleNTrsn3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1448, 673, 220, 20));

        titleNTrsn4.setFont(new java.awt.Font("Microsoft Tai Le", 1, 18)); // NOI18N
        titleNTrsn4.setText("Gromming Kutu");
        add(titleNTrsn4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1448, 765, 220, 20));

        titleNTrsn5.setFont(new java.awt.Font("Microsoft Tai Le", 1, 18)); // NOI18N
        titleNTrsn5.setText("Gromming Kutu");
        add(titleNTrsn5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1448, 860, 220, 20));

        titleNTrsn6.setFont(new java.awt.Font("Microsoft Tai Le", 1, 18)); // NOI18N
        titleNTrsn6.setText("Gromming Kutu");
        add(titleNTrsn6, new org.netbeans.lib.awtextra.AbsoluteConstraints(1448, 952, 220, 20));

        priceNTrsn1.setFont(new java.awt.Font("Microsoft Tai Le", 1, 24)); // NOI18N
        priceNTrsn1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        priceNTrsn1.setText("40,000");
        add(priceNTrsn1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1670, 470, 140, 40));

        priceNTrsn2.setFont(new java.awt.Font("Microsoft Tai Le", 1, 24)); // NOI18N
        priceNTrsn2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        priceNTrsn2.setText("40,000");
        add(priceNTrsn2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1670, 562, 140, 40));

        priceNTrsn3.setFont(new java.awt.Font("Microsoft Tai Le", 1, 24)); // NOI18N
        priceNTrsn3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        priceNTrsn3.setText("40,000");
        add(priceNTrsn3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1670, 655, 140, 40));

        priceNTrsn4.setFont(new java.awt.Font("Microsoft Tai Le", 1, 24)); // NOI18N
        priceNTrsn4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        priceNTrsn4.setText("40,000");
        add(priceNTrsn4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1670, 750, 140, 40));

        priceNTrsn5.setFont(new java.awt.Font("Microsoft Tai Le", 1, 24)); // NOI18N
        priceNTrsn5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        priceNTrsn5.setText("40,000");
        add(priceNTrsn5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1670, 840, 140, 40));

        priceNTrsn6.setFont(new java.awt.Font("Microsoft Tai Le", 1, 24)); // NOI18N
        priceNTrsn6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        priceNTrsn6.setText("40,000");
        add(priceNTrsn6, new org.netbeans.lib.awtextra.AbsoluteConstraints(1670, 935, 140, 40));
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 206, 343, 66));
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 270, 343, 66));
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 350, 343, 66));
        add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 420, 343, 66));
        add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 500, 343, 66));
        add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 570, 343, 66));
        add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 650, 343, 66));
        add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 970, 343, 66));
        add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 720, 343, 66));

        borderHide.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/dashboard.png"))); // NOI18N
        add(borderHide, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        borderShow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/dashboard (1).png"))); // NOI18N
        add(borderShow, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void combobox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combobox1ActionPerformed
        // TODO add your handling code here:
        String data = (String) combobox1.getSelectedItem();
        if (data.equals("All")){
            title1.setText("Client");
            setIconInitAll();
        }else if (data.equals("in Days")){
            title1.setText("Client Hari Ini");
            setIconInitDay();
        } else if (data.equals("in Weeks")) {
            title1.setText("Client Minggu Ini");
            setIconInitWeek();
        } else if (data.equals("in Months")) {
            title1.setText("Client Bulan Ini");
            setIconInitMonth();
        } else if (data.equals("in Years")) {
            title1.setText("Client Tahun Ini");
            setIconInitYear();
        }
    }//GEN-LAST:event_combobox1ActionPerformed

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        super.paintComponent(grphcs);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel borderHide;
    private javax.swing.JLabel borderShow;
    private swing.comboBox.Combobox combobox1;
    private javax.swing.JLabel descRBarang1;
    private javax.swing.JLabel descRBarang2;
    private javax.swing.JLabel descRBarang3;
    private javax.swing.JLabel descRLayanan1;
    private javax.swing.JLabel descRLayanan2;
    private javax.swing.JLabel descRLayanan3;
    private javax.swing.JLabel icon1;
    private javax.swing.JLabel icon2;
    private javax.swing.JLabel icon3;
    private javax.swing.JLabel icon4;
    private javax.swing.JLabel icon5;
    private javax.swing.JLabel icon6;
    private javax.swing.JLabel icon7;
    private javax.swing.JLabel icon8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel priceNTrsn1;
    private javax.swing.JLabel priceNTrsn2;
    private javax.swing.JLabel priceNTrsn3;
    private javax.swing.JLabel priceNTrsn4;
    private javax.swing.JLabel priceNTrsn5;
    private javax.swing.JLabel priceNTrsn6;
    private javax.swing.JLabel sumberNTrsn1;
    private javax.swing.JLabel sumberNTrsn2;
    private javax.swing.JLabel sumberNTrsn3;
    private javax.swing.JLabel sumberNTrsn4;
    private javax.swing.JLabel sumberNTrsn5;
    private javax.swing.JLabel sumberNTrsn6;
    private javax.swing.JLabel title1;
    private javax.swing.JLabel title2;
    private javax.swing.JLabel title3;
    private javax.swing.JLabel title4;
    private javax.swing.JLabel titleNTrsn1;
    private javax.swing.JLabel titleNTrsn2;
    private javax.swing.JLabel titleNTrsn3;
    private javax.swing.JLabel titleNTrsn4;
    private javax.swing.JLabel titleNTrsn5;
    private javax.swing.JLabel titleNTrsn6;
    private javax.swing.JLabel titleRBarang1;
    private javax.swing.JLabel titleRBarang2;
    private javax.swing.JLabel titleRBarang3;
    private javax.swing.JLabel titleRLayanan1;
    private javax.swing.JLabel titleRLayanan2;
    private javax.swing.JLabel titleRLayanan3;
    private javax.swing.JLabel toTransaksi;
    private javax.swing.JLabel txt_client;
    private javax.swing.JLabel txt_laba;
    private javax.swing.JLabel txt_pendapatan;
    private javax.swing.JLabel txt_transaksi;
    // End of variables declaration//GEN-END:variables
}
