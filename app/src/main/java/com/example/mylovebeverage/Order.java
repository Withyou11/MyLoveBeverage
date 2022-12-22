package com.example.mylovebeverage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylovebeverage.Adapters.OrderProductAdapter;
import com.example.mylovebeverage.Adapters.OrderDetailAdapter;
import com.example.mylovebeverage.Models.Product;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Order extends AppCompatActivity {

    Connecting_MSSQL connecting_mssql;
    private static Connection connection_product = null;
    private static Connection connection_invoice = null;
    private static Connection connection_invoice_detail = null;

    GridView gridView;
    public static ListView listView;
    public static OrderDetailAdapter orderDetailAdapter;
    public  static ArrayList<Product> productArrayList = new ArrayList<Product>();
    public  static ArrayList<Product> productArrayList2 = new ArrayList<Product>();
    Product product;
    Button btnOrderFilterAll, btnOrderFilterCoffee, btnOrderFilterMilktea, btnOrderFilterTea,
            btnOrderFilterFreeze, btnOrderFilterOthers, btnOrderFilterCake,
            btnOrderCancel, btnOrderConfirm;
    TextView txtOrderStaffId, txtOrderDate;
    EditText editTextOrderDetailMoneyReceived;
    SearchView searchView;
    public static  TextView txtTotalOrder;
    Integer a = 0;
    public static Integer totalOrderBill = 0;
    String getStaffId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        connecting_mssql = new Connecting_MSSQL(connection_product);
        connection_product = connecting_mssql.Connecting();

        connecting_mssql = new Connecting_MSSQL(connection_invoice);
        connection_invoice = connecting_mssql.Connecting();

        connecting_mssql = new Connecting_MSSQL(connection_invoice_detail);
        connection_invoice_detail = connecting_mssql.Connecting();

        btnOrderFilterAll = (Button) findViewById(R.id.btnOrderFilterAll);
        btnOrderFilterCoffee = (Button) findViewById(R.id.btnOrderFilterCoffee);
        btnOrderFilterMilktea = (Button) findViewById(R.id.btnOrderFilterMilktea);
        btnOrderFilterTea = (Button) findViewById(R.id.btnOrderFilterTea);
        btnOrderFilterFreeze = (Button) findViewById(R.id.btnOrderFilterFreeze);
        btnOrderFilterOthers = (Button) findViewById(R.id.btnOrderFilterOthers);
        btnOrderFilterCake = (Button) findViewById(R.id.btnOrderFilterCake);
        btnOrderCancel = (Button) findViewById(R.id.btnOrderCancel);
        btnOrderConfirm = (Button) findViewById(R.id.btnOrderConfirm);

        txtOrderStaffId = (TextView) findViewById(R.id.txtOrderStaffId);
        txtOrderDate = (TextView) findViewById(R.id.txtOrderDate);
        txtTotalOrder = (TextView) findViewById(R.id.txtOrderDetailTotalDisplay);

        editTextOrderDetailMoneyReceived = (EditText) findViewById(R.id.editTextOrderDetailMoneyReceived);

        searchView = (SearchView) findViewById(R.id.searchViewOrder);
        searchView.setFocusable(false);

        txtTotalOrder.setText(totalOrderBill.toString() + " VND");

        Intent getIntent = getIntent();
        getStaffId = getIntent.getStringExtra("Staff Id");
        Date date = new Date();
        DateFormat dateFormat =  new SimpleDateFormat("dd/MM/yyyy");

        txtOrderStaffId.setText("Staff ID: " + getStaffId);
        txtOrderDate.setText("Date: " + dateFormat.format(date));

        initSearchWidgets();

        getAllProducts();
        setUpGridview();
        setBackGroundButton("");

        orderDetailAdapter = new OrderDetailAdapter(getApplicationContext(),0,productArrayList2);

        btnOrderFilterAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackGroundButton("");
                getAllProducts();
                setUpGridview();
            }
        });

        btnOrderFilterCoffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackGroundButton("CT1");
                getFilterProducts("CT1");
                setUpGridview();
            }
        });

        btnOrderFilterMilktea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackGroundButton("CT2");
                getFilterProducts("CT2");
                setUpGridview();
            }
        });

        btnOrderFilterTea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackGroundButton("CT3");
                getFilterProducts("CT3");
                setUpGridview();
            }
        });

        btnOrderFilterFreeze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackGroundButton("CT4");
                getFilterProducts("CT4");
                setUpGridview();
            }
        });

        btnOrderFilterOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackGroundButton("CT5");
                getFilterProducts("CT5");
                setUpGridview();
            }
        });

        btnOrderFilterCake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackGroundButton("CT6");
                getFilterProducts("CT6");
                setUpGridview();
            }
        });

        setUpList();

        btnOrderCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productArrayList2.isEmpty()){
                    Dialog dialog = new Dialog(Order.this);
                    dialog.setContentView(R.layout.activity_custom_dialog_2);
                    TextView txtMessage = (TextView) dialog.findViewById(R.id.txtCustomDialog2Message);
                    txtMessage.setText("The current order is empty!");
                    dialog.show();
                    Button btnOk = (Button) dialog.findViewById(R.id.btnCustomDialog2Ok);
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
                else {
                    Dialog dialog = new Dialog(Order.this);
                    dialog.setContentView(R.layout.activity_custom_dialog);
                    TextView txtMessage = (TextView) dialog.findViewById(R.id.txtCustomDialogMessage);
                    txtMessage.setText("Do you want to completely delete the current order?");
                    dialog.show();
                    Button btnYes = (Button) dialog.findViewById(R.id.btnCustomDialogYes);
                    Button btnNo = (Button) dialog.findViewById(R.id.btnCustomDialogNo);
                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            productArrayList2.removeAll(productArrayList2);
                            orderDetailAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                            totalOrderBill = 0;
                            txtTotalOrder.setText(totalOrderBill.toString() + " VND");
                            editTextOrderDetailMoneyReceived.setText("");
                        }
                    });
                    btnNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            }
        });

        btnOrderConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productArrayList2.isEmpty()){
                    Dialog dialog = new Dialog(Order.this);
                    dialog.setContentView(R.layout.activity_custom_dialog_2);
                    TextView txtMessage = (TextView) dialog.findViewById(R.id.txtCustomDialog2Message);
                    txtMessage.setText("The current order is empty!");
                    dialog.show();
                    Button btnOk = (Button) dialog.findViewById(R.id.btnCustomDialog2Ok);
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
                else {
                    if (editTextOrderDetailMoneyReceived.getText().toString().matches("")){
                        Dialog dialog = new Dialog(Order.this);
                        dialog.setContentView(R.layout.activity_custom_dialog_2);
                        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtCustomDialog2Message);
                        txtMessage.setText("You must enter the amount of money the customer gives!");
                        dialog.show();
                        Button btnOk = (Button) dialog.findViewById(R.id.btnCustomDialog2Ok);
                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                    else{
                        if(Integer.parseInt(editTextOrderDetailMoneyReceived.getText().toString()) < totalOrderBill){
                            Dialog dialog = new Dialog(Order.this);
                            dialog.setContentView(R.layout.activity_custom_dialog_2);
                            TextView txtMessage = (TextView) dialog.findViewById(R.id.txtCustomDialog2Message);
                            txtMessage.setText("The amount of money received must be greater than the total amount of the order!");
                            dialog.show();
                            Button btnOk = (Button) dialog.findViewById(R.id.btnCustomDialog2Ok);
                            btnOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                        }
                        else {
                            Dialog dialog = new Dialog(Order.this);
                            dialog.setContentView(R.layout.activity_custom_dialog);
                            TextView txtMessage = (TextView) dialog.findViewById(R.id.txtCustomDialogMessage);
                            txtMessage.setText("Do you want to complete the current order?");
                            dialog.show();
                            Button btnYes = (Button) dialog.findViewById(R.id.btnCustomDialogYes);
                            Button btnNo = (Button) dialog.findViewById(R.id.btnCustomDialogNo);
                            btnYes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String numberOfInvoiceFormat = "";
                                    if (connection_invoice!=null)
                                    {
                                        try {
                                            Integer numberOfInvoice = 0;
                                            Date curentTime = new Date();
                                            String timeFormat = "";
                                            Statement statement = connection_invoice.createStatement();
                                            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM INVOICE;");
                                            while (resultSet.next()) {
                                                numberOfInvoice = resultSet.getInt(1) + 1;
                                            }
                                            if (numberOfInvoice < 10){
                                                numberOfInvoiceFormat = "0" + numberOfInvoice;
                                            }
                                            else {
                                                numberOfInvoiceFormat = numberOfInvoice + "";
                                            }
                                            Statement statement2 = connection_invoice.createStatement();
                                            ResultSet resultSet2 = statement2.executeQuery("SELECT CURRENT_TIMESTAMP;");
                                            while (resultSet2.next()) {
                                                curentTime = resultSet2.getTimestamp(1);
                                            }
                                            timeFormat = getDateTimeCustom(curentTime);
                                            a = Integer.parseInt(editTextOrderDetailMoneyReceived.getText().toString()) - totalOrderBill;
                                            Statement statement3 = connection_invoice.createStatement();
                                            ResultSet resultSet3 = statement3.executeQuery("INSERT INTO [dbo].[INVOICE] (Invoice_ID, Staff_ID, DateTime_Invoice, Price_of_Invoice, Money_Received, Money_Returned)\n" +
                                                    "VALUES ('I" + numberOfInvoiceFormat + "', '" + getStaffId + "', '" + timeFormat + "', '" + totalOrderBill + "', " + editTextOrderDetailMoneyReceived.getText().toString() + ", " + a + ");");
                                        }catch (SQLException e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }else
                                    {
                                        Toast.makeText(Order.this, "Connect to InvoiceDB makes error." , Toast.LENGTH_SHORT).show();
                                    }
                                    for (Product product : productArrayList2){
                                        if (connection_invoice_detail!=null)
                                        {
                                            try {
                                                Statement statement = connection_invoice_detail.createStatement();
                                                ResultSet resultSet = statement.executeQuery("INSERT INTO [dbo].[DETAILINVOICE] (Invoice_ID, Product_ID, Quantity)\n" +
                                                        "VALUES ('I" + numberOfInvoiceFormat + "', '" + product.getProduct_ID() + "', '" + product.getOrderAmount() + "');");
                                            }catch (SQLException e)
                                            {
                                                e.printStackTrace();
                                            }
                                        }else
                                        {
                                            Toast.makeText(Order.this, "Connect to DetailInvoiceDB makes error." , Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    dialog.dismiss();
                                    productArrayList2.removeAll(productArrayList2);
                                    orderDetailAdapter.notifyDataSetChanged();
                                    totalOrderBill = 0;
                                    txtTotalOrder.setText(totalOrderBill.toString() + " VND");
                                    editTextOrderDetailMoneyReceived.setText("");
                                    Intent intent = new Intent(getApplicationContext(), PostOrder.class);
                                    intent.putExtra("invoiceId","I" + numberOfInvoiceFormat);
                                    startActivity(intent);
                                }
                            });
                            btnNo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                        }
                    }

                }
            }
        });
    }

    private  void initSearchWidgets(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Product> searchedProductArrayList = new ArrayList<Product>();
                for (Product product : productArrayList){
                    if(product.getName_of_Product().toLowerCase().contains(newText.toLowerCase())){
                        searchedProductArrayList.add(product);
                    }
                }

                OrderProductAdapter searchProductAdapter = new OrderProductAdapter(getApplicationContext(),R.layout.activity_grid_order_product_item,searchedProductArrayList);
                gridView.setAdapter(searchProductAdapter);

                return false;
            }
        });
    }

    private void getAllProducts() {
        if (connection_product!=null)
        {
            try {
                Statement statement = connection_product.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM PRODUCT;");
                productArrayList.removeAll(productArrayList);
                while (resultSet.next()) {
                    product = new Product(resultSet.getString(1).trim(),resultSet.getString(2).trim(),resultSet.getString(3).trim(),resultSet.getString(4).trim(),resultSet.getInt(7),resultSet.getString(8).trim(),resultSet.getInt(5),resultSet.getString(6));
                    productArrayList.add(product);
                }

            }catch (SQLException e)
            {
                e.printStackTrace();
            }
        }else
        {
            Toast.makeText(getApplicationContext(), "Connect to ProductDB makes error." , Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpGridview() {
        gridView = findViewById(R.id.gridViewOrderProduct);
        OrderProductAdapter orderProductAdapter = new OrderProductAdapter(this,R.layout.activity_grid_order_product_item,productArrayList);
        gridView.setAdapter(orderProductAdapter);
        orderProductAdapter.notifyDataSetChanged();
    }

    private void getFilterProducts(String a) {
        if (connection_product!=null)
        {
            try {
                Statement statement = connection_product.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM PRODUCT WHERE Category_ID = '" + a + "';");
                productArrayList.removeAll(productArrayList);
                while (resultSet.next()) {
                    product = new Product(resultSet.getString(1).trim(),resultSet.getString(2).trim(),resultSet.getString(3).trim(),resultSet.getString(4).trim(),resultSet.getInt(7),resultSet.getString(8).trim(),resultSet.getInt(5),resultSet.getString(6));
                    productArrayList.add(product);
                }

            }catch (SQLException e)
            {
                e.printStackTrace();
            }
        }else
        {
            Toast.makeText(getApplicationContext(), "Connect to ProductDB makes error." , Toast.LENGTH_SHORT).show();
        }
    }

    private void setBackGroundButton(String status) {
        btnOrderFilterAll.setBackgroundResource(R.drawable.custom_order_filter_button);
        btnOrderFilterCoffee.setBackgroundResource(R.drawable.custom_order_filter_button);
        btnOrderFilterMilktea.setBackgroundResource(R.drawable.custom_order_filter_button);
        btnOrderFilterTea.setBackgroundResource(R.drawable.custom_order_filter_button);
        btnOrderFilterFreeze.setBackgroundResource(R.drawable.custom_order_filter_button);
        btnOrderFilterOthers.setBackgroundResource(R.drawable.custom_order_filter_button);
        btnOrderFilterCake.setBackgroundResource(R.drawable.custom_order_filter_button);
        switch ( status ) {
            case  "CT1":
                btnOrderFilterCoffee.setBackgroundResource(R.drawable.custom_selected_order_filter_button);
                break;

            case  "CT2":
                btnOrderFilterMilktea.setBackgroundResource(R.drawable.custom_selected_order_filter_button);
                break;

            case  "CT3":
                btnOrderFilterTea.setBackgroundResource(R.drawable.custom_selected_order_filter_button);
                break;

            case  "CT4":
                btnOrderFilterFreeze.setBackgroundResource(R.drawable.custom_selected_order_filter_button);
                break;

            case  "CT5":
                btnOrderFilterOthers.setBackgroundResource(R.drawable.custom_selected_order_filter_button);

                break;
            case  "CT6":
                btnOrderFilterCake.setBackgroundResource(R.drawable.custom_selected_order_filter_button);
                break;
            default:
                btnOrderFilterAll.setBackgroundResource(R.drawable.custom_selected_order_filter_button);
        }

    }

    public void setUpList() {
        listView = (ListView) findViewById(R.id.orderDetailListView);
        listView.setAdapter(orderDetailAdapter);
        orderDetailAdapter.notifyDataSetChanged();
    }

    public String getDateTimeCustom(Date a)
    {
        String temp = "";
        String hour = "";
        String result = "";
        DateFormat dateFormat = null;
        dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        temp = dateFormat.format(a);
        hour = temp.charAt(9) + "" + temp.charAt(10);
        result = temp.substring (0,9) + (Integer.parseInt(hour) + 7) + temp.substring(11);
        return result;
    }
}