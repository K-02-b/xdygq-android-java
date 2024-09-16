package com.example.xdygq3;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Functions {

    public static void addTextRow(Context context, TableRow tableRow, Classes.Compat[] Compats) {
        for (Classes.Compat item : Compats) {
            TextView textView = new TextView(context);
            textView.setTag(item.tag);
            textView.setText(item.content);
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10, 10, 10, 10);
            textView.setLayoutParams(layoutParams);
            textView.setPadding(10, 10, 10, 10);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            textView.setId(item.id);
            tableRow.addView(textView);
        }
    }

    public static void addEditRow(Context context, TableRow tableRow, Classes.Compat[] Compats) {
        for (Classes.Compat item : Compats) {
            EditText editText = new EditText(context);
            editText.setTag(item.tag);
            editText.setText(item.content);
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10, 10, 10, 10);
            editText.setLayoutParams(layoutParams);
            editText.setPadding(10, 10, 10, 10);
            editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            editText.setId(item.id);
            tableRow.addView(editText);
        }
    }

    public static String getFile(Context context, String filename) {
        try {
            FileInputStream fis = context.openFileInput(filename);
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();
            System.out.println(new String(data));
            return new String(data);
        } catch (Exception e) {
            try {
                String contentData = "";
                FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
                fos.write(contentData.getBytes());
                fos.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            e.printStackTrace();
        }
        return null;
    }

    public static void PutFile(Context context, String filename, String content) {
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(content.getBytes());
            fos.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean checkFileExists(Context context, String fileName) {
        try {
            // 尝试打开文件
            context.openFileInput(fileName);
//            Log.d("FileCheck", "文件存在：" + fileName);
            return true;
        } catch (FileNotFoundException e) {
//            Log.d("FileCheck", "文件不存在：" + fileName);
            return false;
        }
    }

    public static CharSequence menuIconWithText(Drawable r, String title) {
        r.setBounds(0, 0, r.getIntrinsicWidth(), r.getIntrinsicHeight());
        SpannableString sb = new SpannableString("    " + title);
        ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
}
