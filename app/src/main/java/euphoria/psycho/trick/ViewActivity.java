package euphoria.psycho.trick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        Intent intent = getIntent();

        TextView textView = findViewById(R.id.textView);

        textView.setText(Databases.getInstance().getTrick(intent.getIntExtra("id", 0)));
    }
}
