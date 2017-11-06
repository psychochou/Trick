package euphoria.psycho.trick;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class ViewFragment extends Fragment {

    private static final int ID_ADD_TRICK = 10;
    private ListView mListView;
    private String mTag;
    private TrickAdapter mTrickAdapter;

    private void addTrick() {

        Intent intent = new Intent(getActivity(), EditorActivity.class);

        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fetchList();
    }

    private void fetchList() {
        mTrickAdapter.switchData(Databases.getInstance().getTricks(mTag));

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        setHasOptionsMenu(true);
        Bundle bundle = getArguments();
        mTag = bundle.getString("tag");

        View view = inflater.inflate(R.layout.fragment_view, container, false);

        mListView = view.findViewById(R.id.listView);
        registerForContextMenu(mListView);
        mTrickAdapter = new TrickAdapter(Databases.getInstance().getTricks(mTag), getActivity());
        mListView.setAdapter(mTrickAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ViewActivity.class);
                intent.putExtra("id", mTrickAdapter.getItem(i).Id);
                startActivity(intent);
            }
        });

        ;
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(Menu.NONE, ID_ADD_TRICK, 0, "添加");

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case ID_ADD_TRICK:
                addTrick();
                break;

        }
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        List<String> collection = Databases.getInstance().getTabList();

        menu.add(Menu.NONE, 100, 0, "编辑 ");


        for (int i = 0; i < collection.size(); i++) {
            menu.add(Menu.NONE, i + 100, 0, "移动到" + collection.get(i));
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (getUserVisibleHint()) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            if (item.getItemId() == 100) {
                Intent intent = new Intent(getActivity(), EditorActivity.class);
                intent.putExtra("id", mTrickAdapter.getItem(info.position).Id);
                startActivityForResult(intent, 0);
                return true;
            } else {
                Databases.getInstance().moveTrick(mTrickAdapter.getItem(info.position).Id, item.getTitle().toString().substring(3));
                fetchList();
                return true;
            }
        }
        return false;

    }

    private static class ViewHolder {
        TextView textView;
    }

    private static class TrickAdapter extends BaseAdapter {

        private final Context mContext;
        private final List<TrickModel> mList;

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public TrickModel getItem(int i) {
            return mList == null || mList.size() == 0 ? null : mList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;

            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.list_item, viewGroup, false);
                viewHolder = new ViewHolder();
                viewHolder.textView = view.findViewById(R.id.textView);
                view.setTag(viewHolder);
            } else {

                viewHolder = (ViewHolder) view.getTag();
            }

            viewHolder.textView.setText(mList.get(i).Title);
            return view;
        }

        public void switchData(List<TrickModel> models) {
            mList.clear();
            mList.addAll(models);
            notifyDataSetChanged();
        }

        private TrickAdapter(List<TrickModel> list, Context context) {
            mList = list;
            mContext = context;
        }
    }
}
