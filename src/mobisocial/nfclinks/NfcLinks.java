package mobisocial.nfclinks;

import org.openjunction.nfcflix.R;

import mobisocial.nfc.Nfc;
import android.app.ListActivity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class NfcLinks extends ListActivity {
	private static final String TAG = "nfclinks";
	private Nfc mNfc;
	
	private int mSelection = -1;
	NfcLink[] mLinks = new NfcLink[] {
		//new NfcLink("Nothing", null),
		new NfcLink("Nfc: TV Demo (Video)", "http://www.youtube.com/watch?v=80uKAib_FCI"),
		new NfcLink("Nfc: Whiteboard Demo (Video)", "http://www.youtube.com/watch?v=WEj4QH9f8a8"),
		new NfcLink("Nfc: Photo Demo (Video)", "http://www.youtube.com/watch?v=CcNl6czz79U"),
		new NfcLink("GMail", "http://mail.google.com"),
		new NfcLink("MobiSocial", "http://mobisocial.stanford.edu/news"),
		new NfcLink("The Manatee", "http://manatee.servemp3.com/music/index.php?user=ben&pw_hashed=true&pass=c087684276231f074402d2aef04c6597"),
		new NfcLink("Engadget", "http://engadet.com"),
		new NfcLink("Slashdot", "http://slashdot.org"),
		new NfcLink("Ars Technica", "http://arstechnica.com"),
		new NfcLink("CNN", "http://cnn.com"),
		new NfcLink("New York Times", "http://nytimes.com"),
	};
	
	private ListAdapter mListAdapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNfc = new Nfc(this);
        mListAdapter = new ArrayAdapter<NfcLink>(this, android.R.layout.simple_list_item_1, mLinks);
        setContentView(R.layout.main);
        setListAdapter(mListAdapter);
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	Log.d(TAG, "pause called");
    	mNfc.onPause(this);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	mNfc.onResume(this);
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
    	if (mNfc.onNewIntent(this, intent)) return;
    }
    
    @Override protected void onListItemClick(ListView l, View v, int position, long id) {
    	mSelection = position;
    	NfcLink selection = mLinks[mSelection];
    	NdefMessage ndef = selection.getNdefMessage();
		//mNfcAdapter.enableForegroundNdefPush(this, ndef);
    	mNfc.share(ndef);
    	((TextView)findViewById(R.id.selection)).setText("Now sharing: " + selection.getName() + ".");
    };
    
        
    class NfcLink {
    	String name;
    	String url;
    	public NfcLink(String name, String url) {
    		this.name = name;
    		this.url = url;
    	}
    	
    	public String getName() {
    		return name;
    	}
    	
    	public String getId() {
    		return url;
    	}
    	
    	public String getUrl() {
    		return this.url;
    	}
    	
    	public NdefMessage getNdefMessage() {
    		if (url == null) {
    			return null;
    		}
    		NdefRecord urlRecord = new NdefRecord(NdefRecord.TNF_ABSOLUTE_URI, NdefRecord.RTD_URI, new byte[] {}, getUrl().getBytes());
    		NdefMessage ndef = new NdefMessage(new NdefRecord[] { urlRecord });
    		return ndef;
    	}
    	
    	@Override
    	public String toString() {
    		return name;
    	}
    }
}