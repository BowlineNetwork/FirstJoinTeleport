package net.bowline.firstjointeleport.Util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;

public class SQL {
    public static boolean tableExists(final String table) {
        if (table == null)
            return false;
        try {
            final Connection connection = MySQL.getConnection();
            if (connection == null)
                return false;
            final DatabaseMetaData metadata = connection.getMetaData();
            if (metadata == null)
                return false;
            final ResultSet rs = metadata.getTables(null, null, table, null);
            if (rs.next())
                return true;
        }
        catch (final Exception ex) {}
        return false;
    }

    public static void insertData(final String columns, final String values, final String table) {
        try {
            MySQL.update("INSERT INTO " + table + " (" + columns + ") VALUES (" + values + ");");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteData(final String column, final String logic_gate, String data, final String table) {
        if (data != null)
            data = "'" + data + "'";
        try {
            MySQL.update("DELETE FROM " + table + " WHERE " + column + logic_gate + data + ";");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean exists(final String column, String data, final String table) {
        if (data != null)
            data = "'" + data + "'";
        try {
            final ResultSet rs = MySQL.query("SELECT * FROM " + table + " WHERE " + column + "=" + data + ";");
            while (rs.next())
                if (rs.getString(column) != null)
                    return true;
        }
        catch (final Exception ex) {}
        return false;
    }

    public static void deleteTable(final String table) {
        try {
            MySQL.update("DROP TABLE " + table + ";");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void truncateTable(final String table) {
        try {
            MySQL.update("TRUNCATE TABLE " + table + ";");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createTable(final String table, final String columns) {
        if (!tableExists(table)) {
            try {
                MySQL.update("CREATE TABLE " + table + " (" + columns + ");");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void set(final String selected, Object object, final String column, final String logic_gate, String data, final String table) {
        if (object != null)
            object = "'" + object + "'";
        if (data != null)
            data = "'" + data + "'";
        try {
            MySQL.update("UPDATE " + table + " SET " + selected + "=" + object + " WHERE " + column + logic_gate + data + ";");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void edit(final String selected, final int newData, final String column, final String logic_gate, String data, final String table) {
        if (data != null)
            data = "'" + data + "'";
        try {
            MySQL.update("UPDATE `" + table + "` SET " + selected + " " + logic_gate + " '" + newData + "' WHERE " + column + " = '" + data + "'");
        } catch (final Exception ex) { ex.printStackTrace(); }
    }

    public static Object get(final String selected, final String column, final String logic_gate, String data, final String table) {
        if (data != null)
            data = "'" + data + "'";
        try {
            final ResultSet rs = MySQL.query("SELECT * FROM " + table + " WHERE " + column + logic_gate + data + ";");
            if (rs.next())
                return rs.getObject(selected);
        }
        catch (final Exception ex) {}
        return null;
    }

    public static ArrayList<Object> listGet(final String selected, final String column, final String logic_gate, String data, final String table) {
        final ArrayList<Object> array = new ArrayList<>();
        if (data != null)
            data = "'" + data + "'";
        try {
            final ResultSet rs = MySQL.query("SELECT * FROM " + table + " WHERE " + column + logic_gate + data + ";");
            while (rs.next())
                array.add(rs.getObject(selected));
        }
        catch (final Exception ex) {}
        return array;
    }

    public static int returnHighest(final String table, final String column) {
        int i = 0;

        if (table == null || column == null)
            return i;

        ResultSet rs = null;
        try {
            rs = MySQL.query("SELECT MAX(" + column + ") AS max_id FROM " + table);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (rs.next())
                return rs.getInt("max_id") + 1;
        }

        catch (final Exception ex) {}
        return i;
    }

    public static int countRows(final String table) {
        int i = 0;
        if (table == null)
            return i;
        ResultSet rs = null;
        try {
            rs = MySQL.query("SELECT * FROM " + table + ";");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            while (rs.next())
                ++i;
        }
        catch (final Exception ex) {}
        return i;
    }
}
