package kwygonjin.com.homeinternetradio;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class RadioItemActivity extends AppCompatActivity {
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_add_from_app)
    Button btnAddFromApp;
    @BindView(R.id.et_radiostation_name)
    EditText etRadioName;
    @BindView(R.id.et_radiostation_url)
    EditText etRadioURL;
    @BindView(R.id.et_radiostation_img)
    EditText etRadioImg;
    @BindView(R.id.et_radiostation_genre)
    EditText etRadioGenre;
    @BindView(R.id.iv_content_radiologo)
    ImageView ivRadioLogo;
    @BindView(R.id.rb_img_path)
    RadioButton rbImgPath;
    @BindView(R.id.rb_img_url)
    RadioButton rbImgURL;
    @BindView(R.id.rg_img)
    RadioGroup rgImg;

    private int FILE_SELECT_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        rgImg.check(R.id.rb_img_path);
    }

    @OnClick(R.id.btn_save)
    void saveRadioItemInRealm (){
        if (isEmpty(etRadioName)){
            Toast.makeText(this, "You must fill in a name of radiostation!", Toast.LENGTH_LONG).show();
            return;
        }

        if (isEmpty(etRadioURL)){
            Toast.makeText(this, "You must fill in a URL of radiostation!", Toast.LENGTH_LONG).show();
            return;
        }

        RealmDBManager.insertRadio(Realm.getDefaultInstance(), this,
                etRadioName.getText().toString(),
                R.drawable.radio_img_placeholder,
                false,
                etRadioGenre.getText().toString(),
                etRadioURL.getText().toString());

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @OnClick(R.id.et_radiostation_img)
    void selectImgFromFile (){
        if (rgImg.getCheckedRadioButtonId() != R.id.rb_img_path)
            return;

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //intent.setType("*/*");      //all files
        intent.setType("*/*");   //XML file only

        try {
            startActivityForResult(Intent.createChooser(intent, "Select image file"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btn_add_from_app)
    void addRadioItemFromApp (){
        Toast.makeText(this, "Not work yet :(", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.btn_cancel)
    void cancelAddingRadioItem (){
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == FILE_SELECT_CODE) {

            }
        }
    }
}
