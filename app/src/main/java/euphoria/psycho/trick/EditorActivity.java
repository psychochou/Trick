package euphoria.psycho.trick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import org.javia.arity.Symbols;
import org.javia.arity.SyntaxException;
import org.javia.arity.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditorActivity extends Activity {
    TrickModel mTrickModel;
    EditText mEditText;
    private Symbols mSymbols;
    private int mLineLength = 0;

    private static final int MENU_ID_CALCULATE = 0X1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        mEditText = findViewById(R.id.editText);
        Intent intent = getIntent();

        if (intent != null) {
            int id = intent.getIntExtra("id", 0);
            if (id > 0) {
                mTrickModel = new TrickModel();
                mTrickModel.Id = id;
                mEditText.setText(Databases.getInstance().getTrick(id));
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateTrickModel();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_ID_CALCULATE, 0, "计算");

        return super.onCreateOptionsMenu(menu);
    }

    private void updateTrickModel() {
        if (mTrickModel == null) {
            mTrickModel = new TrickModel();
        }
        mTrickModel.Content = mEditText.getText().toString().trim();
        if (mTrickModel.Content.length() > 0) {
            mTrickModel.Title = mTrickModel.Content.split("\n")[0].trim();
            if (mTrickModel.Id > 0)
                Databases.getInstance().updateTrick(mTrickModel);
            else {
                mTrickModel.Tag = "Others";

                Databases.getInstance().addTrick(mTrickModel);
                finish();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ID_CALCULATE:
                evaluate();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void evaluate() {

        if (mSymbols == null) {
            mSymbols = new Symbols();
        }

        String input = mEditText.getText().toString();

        Pattern pattern = Pattern.compile("[0-9\\+\\-\\*\\.\\(\\)\\=/]+");
        Matcher matcher = pattern.matcher(input.split("\\={5}")[0]);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(input).append("\n\n\n=====\n\n\n");
        List<Double> results = new ArrayList<>();
        while (matcher.find()) {
            stringBuilder.append(matcher.group()).append(" => ");
            try {
                String result = Util.doubleToString(mSymbols.eval(matcher.group()), -1);
                results.add(Double.parseDouble(result));
                stringBuilder.append(result).append("\n\n");
            } catch (SyntaxException e) {
                stringBuilder.append(e.message);
            }
        }
        double addAll = 0;

        for (double i : results) {
            addAll += i;
        }
        stringBuilder.append("相加总结果：").append(addAll).append("\n\n\n");
        mEditText.setText(stringBuilder.toString());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
    }
}
