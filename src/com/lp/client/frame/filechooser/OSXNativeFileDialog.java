/**
 * Based on Steve Hannah's Code at https://gist.github.com/shannah/65007754c2b0f8add4f7
 * 
 * Added an AccessoryView for filetypes. Especially usefull for save dialogs.
 */
package com.lp.client.frame.filechooser;

import static ca.weblite.objc.RuntimeUtils.msg;
import static ca.weblite.objc.RuntimeUtils.msgString;
import static ca.weblite.objc.RuntimeUtils.sel;
import static ca.weblite.objc.util.CocoaUtils.dispatch_sync;

import java.awt.FileDialog;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.filechooser.FileFilter;

import com.lp.client.frame.filechooser.filter.HvTaggedFileFilter;
import com.lp.client.util.cocoa.foundation.NSPoint;
import com.lp.client.util.cocoa.foundation.NSRect;
import com.lp.client.util.cocoa.foundation.NSSize;
import com.lp.util.Pair;
import com.sun.jna.Pointer;

import ca.weblite.objc.NSObject;
import ca.weblite.objc.Proxy;
import ca.weblite.objc.annotations.Msg;

/**
 * A wrapper around NSSavePanel and NSOpenPanel with some methods similar to java.awt.FileDialog.
 * 
 * <p>This class includes wrappers for most settings of both NSSavePanel and NSOpen panel so that
 * you have full flexibility (e.g. select directories only, files only, force a certain extension,
 * allow user to add folders, show hidden files, etc...</p>
 * <p><b>NOTE:</b> Don't run the NativeFileDialog on the Swing EDT as it will cause a deadlock.  Check out the 
 * example_directory_dialog.java snippet for an example using FoxTrot to do a modal dialog in a safe way.</p>
 * 
 * <h3>Example Save Panel</h3>
 * <code><pre>
 *  NativeFileDialog dlg = new NativeFileDialog("Save as...", FileDialog.SAVE);
    dlg.setMessage("Will save only as .txt file");
    dlg.setExtensionHidden(true);
    dlg.setAllowedFileTypes(Arrays.asList("txt"));
    dlg.setVisible(true);  // this is modal
    String f = dlg.getFile();
    if ( f == null ){
        return;
    }
    File file = new File(f);
    saveFile(file);
 * </pre></code>
 * 
 * <h3>Example Open Panel</h3>
 * 
 * <code><pre>
 *  NativeFileDialog dlg = new NativeFileDialog("Select file to open", FileDialog.LOAD);
    dlg.setVisible(true); // this is modal.
    String f = dlg.getFile();
    if ( f != null ){
        openFile(new File(f));
    }
 * </pre></code>
 * @author shannah
 * @see <a href="https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/ApplicationKit/Classes/nssavepanel_Class/Reference/Reference.html">NSSavePanel Class Reference</a>
 * @see <a href="https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/ApplicationKit/Classes/nsopenpanel_Class/Reference/Reference.html">NSOpenPanel Class Reference</a>
 * @see <a href="http://docs.oracle.com/javase/7/docs/api/java/awt/FileDialog.html">java.awt.FileDialog API docs</a>
 */
public class OSXNativeFileDialog extends NSObject {
    private List<Pair<String, String>> chooseableFilters = new ArrayList<Pair<String,String>>();
    private List<FilterInfo> betterFilters = new ArrayList<FilterInfo>();
    
    private int selectedFilterIndex;
    
    /**
     * Reference to the Obj-C NSOpenPanel or NSSavePanel class.
     */
    private Proxy peer;
    
    /**
     * Either FileDialog.LOAD or FileDialog.SAVE
     */
    private int mode;
    
    
    /**
     * Creates a new file dialog with the specified title and mode.
     * @param title The title for the dialog.
     * @param mode Whether to be an open panel or save panel.  Either java.awt.FileDialog.SAVE
     * or java.awt.FileDialog.LOAD
     */
    public OSXNativeFileDialog(final String title, final int mode){
        super("NSObject");
        this.mode = mode;
        dispatch_sync(new Runnable(){

            @Override
            public void run() {
                if (mode == FileDialog.LOAD ){
                    peer = getClient().sendProxy("NSOpenPanel", "openPanel");
                } else {
                    peer = getClient().sendProxy("NSSavePanel", "savePanel");
                }
                peer.send("setTitle:", title);
                peer.send("retain");
            }
        });
    }
    
    
    /**
     * Sets a given selector with a string value on the main thread.
     * @param selector An objective-c selector on the NSSavePanel object. e.g. "setTitle:"
     * @param value The value to set the selector.
     */
    private void set(final String selector, final String value){
        dispatch_sync(new Runnable(){

            @Override
            public void run() {
                System.out.println(peer.getPeer());
                if ( peer.getPeer().equals(Pointer.NULL)){
                    
                    throw new RuntimeException("The peer is null");
                }
//                System.out.println("Before send "+selector+" : "+value);
                peer.send(sel(selector), value);
//                System.out.println("After send");
            }           
        });
    }
    
    
    /**
     * Sets a given selector with an int value on the main thread.
     * @param selector An objective-c selector on the NSSavePanel object. e.g. "setShowsHiddenFiles:"
     * @param value The int value to set.
     */
    private void set(final String selector, final int value){
        dispatch_sync(new Runnable(){
            @Override
            public void run() {
//                System.out.println("About to send to selector: "+selector);
                peer.send(selector, value);
            }          
        });
    }
    
    /**
     * Returns the result of a selector  on the main thread for the NSSavePanel
     * object.
     * @param selector The selector to be run.  e.g. "title".
     * @return The result of calling the given selector on the NSSavePanel object.
     */
    private String getString(final String selector){
        final String[] out = new String[1];
        dispatch_sync(new Runnable(){

            @Override
            public void run() {
                out[0] = peer.sendString(selector);
            }
            
        });
        return out[0];
    }
    
    /**
     * Returns the result of running a selector on the NSSavePanel on the main thread.
     * @param selector The selector to be run.  E.g. "showsHiddenFiles"
     * @return The int result.
     */
    private int getI(final String selector){
        final int[] out = new int[1];
        dispatch_sync(new Runnable(){

            @Override
            public void run() {
                out[0] = peer.sendInt(sel(selector));
            }
            
        });
        return out[0];
    }
    
    /**
     * Sets title of the dialog.
     * @param title The title.
     * @see https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/ApplicationKit/Classes/nssavepanel_Class/Reference/Reference.html
     */
    public void setTitle(String title){
        set("setTitle:", title);
    }
    
    /**
     * Gets the title of the dialog.
     * @return The title
     * @see https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/ApplicationKit/Classes/nssavepanel_Class/Reference/Reference.html
     */
    public String getTitle(){
        return getString("title");
    }
    
    /**
     * @see https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/ApplicationKit/Classes/nssavepanel_Class/Reference/Reference.html
     * @param prompt 
     */
    public void setPrompt(String prompt ){
        set("setPrompt:", prompt);
    }
    
    
    /**
     * @see https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/ApplicationKit/Classes/nssavepanel_Class/Reference/Reference.html
     * @param label 
     */
    public void setNameFieldLabel(String label){
        set("setNameFieldLabel:", label);
    }
    
    
    /**
     * @see https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/ApplicationKit/Classes/nssavepanel_Class/Reference/Reference.html
     * @return 
     */
    public String getNameFieldLabel(){
        return getString("nameFieldLabel");
    }
    
    /**
     * @see https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/ApplicationKit/Classes/nssavepanel_Class/Reference/Reference.html
     * @param message 
     */
    public void setMessage(String message){
        set("setMessage:", message);
    }
    
    
    /**
     * @see https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/ApplicationKit/Classes/nssavepanel_Class/Reference/Reference.html
     * @return 
     */
    public String getMessage(){
        return getString("message");
    }
    
    /**
     * @see https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/ApplicationKit/Classes/nssavepanel_Class/Reference/Reference.html
     * @return 
     */
    public String getPrompt(){
        return getString("prompt");
    }
    
    /**
     * @see https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/ApplicationKit/Classes/nssavepanel_Class/Reference/Reference.html
     * @return 
     */
    public boolean canCreateDirectories(){
        return getI("canCreateDirectories") != 0;
    }
    
    /**
     * @see https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/ApplicationKit/Classes/nssavepanel_Class/Reference/Reference.html
     * @param can 
     */
    public void setCanCreateDirectories(boolean can){
        set("setCanCreateDirectories:", can?1:0);
    }
    
    /**
     * @see https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/ApplicationKit/Classes/nssavepanel_Class/Reference/Reference.html
     * @return 
     */
    public boolean showsHiddenFiles(){
        return getI("showsHiddenFiles") != 0;
    }
    
    
    /**
     * @see https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/ApplicationKit/Classes/nssavepanel_Class/Reference/Reference.html
     * @param shows 
     */
    public void setShowsHiddenFiles(boolean shows){
        set("setShowsHiddenFiles:", shows?1:0);
    }
    
    
    /**
     * @see https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/ApplicationKit/Classes/nssavepanel_Class/Reference/Reference.html
     * @return 
     */
    public boolean isExtensionHidden(){
        return getI("isExtensionHidden")!=0;
    }
    
    /**
     * @see https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/ApplicationKit/Classes/nssavepanel_Class/Reference/Reference.html
     * @param hidden 
     */
    public void setExtensionHidden(boolean hidden){
        set("setExtensionHidden:", hidden?1:0);
    }
    
    /**
     * @see https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/ApplicationKit/Classes/nssavepanel_Class/Reference/Reference.html
     * @return 
     */
    public boolean canSelectHiddenExtension(){
        return getI("canSelectHiddenExtension")!=0;
    }
    
    /**
     * @see https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/ApplicationKit/Classes/nssavepanel_Class/Reference/Reference.html
     * @param sel 
     */
    public void setCanSelectHiddenExtension(boolean sel){
        set("setCanSelectHiddenExtension:", sel?1:0);
    }
    
    /**
     * @see https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/ApplicationKit/Classes/nssavepanel_Class/Reference/Reference.html
     * @param val 
     */
    public void setNameFieldStringValue(String val){
        set("setNameFieldStringValue:", val);
    }
    
    /**
     * @see https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/ApplicationKit/Classes/nssavepanel_Class/Reference/Reference.html
     * @return 
     */
    public List<String> getAllowedFileTypes(){
        final List<String> out = new ArrayList<String>(); 
        dispatch_sync(new Runnable(){

            @Override
            public void run() {
                Proxy types = peer.sendProxy("allowedFileTypes");
                int size = types.getInt("count");
                for ( int i=0; i<size; i++){
                    String nex = types.sendString("objectAtIndex:", i);
                    out.add(nex);
                }
            }
            
        });
        
        return out;
    }
    
    
    /**
     * @see https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/ApplicationKit/Classes/nssavepanel_Class/Reference/Reference.html
     * @param types 
     */
    public void setAllowedFileTypes(final List<String> types){
        dispatch_sync(new Runnable(){

            @Override
            public void run() {
            	setAllowedFileTypesImpl(types);
/*            	
                Proxy mutableArray = getClient().sendProxy("NSMutableArray", "arrayWithCapacity:", types.size());
                
                for (String type: types){
                    
                    mutableArray.send("addObject:", type);
                    
                }
                
                peer.send("setAllowedFileTypes:", mutableArray);
                
                //mutableArray.send("release");
        
*/
            }
            
        });
    }
    
    private void setAllowedFileTypesImpl(final List<String> types) {
        Proxy mutableArray = getClient().sendProxy("NSMutableArray", "arrayWithCapacity:", types.size());
        
        for (String type: types){         
            mutableArray.send("addObject:", type);
        }
        
        peer.send("setAllowedFileTypes:", mutableArray);   	
    }
    
    
    /**
     * @see https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/ApplicationKit/Classes/nssavepanel_Class/Reference/Reference.html
     * @return 
     */
    public boolean allowsOtherFileTypes(){
        return getI("allowsOtherFileTypes")!=0;
    }
    
    /**
     * @see https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/ApplicationKit/Classes/nssavepanel_Class/Reference/Reference.html
     * @param allowed 
     */
    public void setAllowsOtherFileTypes(boolean allowed){
        set("setAllowsOtherFileTypes:", allowed?1:0);
    }
    
    
    /**
     * @see https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/ApplicationKit/Classes/nssavepanel_Class/Reference/Reference.html
     * @return 
     */
    public boolean getTreatsFilePackagesAsDirectories(){
        return getI("treatsFilePackagesAsDirectories")!=0;
    }
    
    
    /**
     * @see https://developer.apple.com/library/mac/#documentation/Cocoa/Reference/ApplicationKit/Classes/nssavepanel_Class/Reference/Reference.html
     * @param treat 
     */
    public void setTreatsFilePackagesAsDirectories(boolean treat){
        set("setTreatsFilePackagesAsDirectories:", treat?1:0);
    }
    
    
    /**
     * Returns the path to the directory that was selected.
     * @return 
     */
    public String getDirectory(){
        final String[] out = new String[1];
        dispatch_sync(new Runnable(){

            @Override
            public void run() {
                Proxy dirUrl = peer.sendProxy("directoryURL");
                if ( dirUrl.getPeer().equals(Pointer.NULL)){
                    out[0] = null;
                } else {
                    out[0] = dirUrl.sendString("path");
                }
                
            }
            
        });
        return out[0];
    }
    
    /**
     * Gets the path to the file that was selected.
     * @return 
     */
    public String getFile(){
        
        final String[] out = new String[1];
        dispatch_sync(new Runnable(){

            @Override
            public void run() {
                Proxy fileUrl = peer.sendProxy("URL");
                System.out.println("File URL :"+fileUrl.getPeer());
                if ( fileUrl == null || fileUrl.getPeer().equals(Pointer.NULL)){
                    out[0] = null;
                } else {
                    System.out.println("Getting path for file URL "+fileUrl.getPeer());
                    long p = msg(fileUrl.getPeer(), "path");
                    System.out.println("Result of path is "+p);
                    String path = msgString(new Pointer(p), "UTF8String" );
                    System.out.println("Actual path is "+path);
                    out[0] = fileUrl.sendString("path");
                }
            }
            
        });
        return out[0];
        
    }
    
    /**
     * Returns an array of files that were selected by the user.
     * @return 
     */
    public File[] getFiles(){
        final List<File> out = new ArrayList<File>();
        dispatch_sync(new Runnable(){

            @Override
            public void run() {
                if ( mode == FileDialog.LOAD ){
                    Proxy nsArray = peer.getProxy("URLs");
                    if ( !nsArray.getPeer().equals(Pointer.NULL)){
                        int size = nsArray.sendInt("count");
                        for ( int i=0; i<size; i++){
                            Proxy url = nsArray.sendProxy("objectAtIndex:", i);
                            String path = url.sendString("path");
                            out.add(new File(path));
                        }
                    } 
                }
            }
            
        });
        
        return out.toArray(new File[0]);
        
    }
    
    /**
     * Returns the mode of this dialog.  
     * @return either FileDialog.LOAD or FileDialog.SAVE
     */
    public int getMode(){
        return mode;
    }
    
    /**
     * Returns true if the dialog allows multiple selection.
     * @return 
     */
    public boolean isMultipleMode(){
        return getI("allowsMultipleSelection")!=0;
    }
    
    
    /**
     * Sets whether the dialog allows the user to select multiple files or not.
     * @param enable 
     */
    public void setMultipleMode(boolean enable){
        set("setAllowsMultipleSelection:", enable?1:0);
    }
    
    /**
     * Returns whether the user can select files in this dialog.
     * @return 
     */
    public boolean canChooseFiles(){
        return getI("canChooseFiles")!=0;
    }
    
    
    /**
     * Sets whether the user can select files in this dialog.
     * @param can 
     */
    public void setCanChooseFiles(boolean can){
        set("setCanChooseFiles:", can?1:0);
    }
    
    public boolean getCanChooseDirectories(){
        return getI("canChooseDirectories")!=0;
    }
    
    public void setCanChooseDirectories(boolean can){
        set("setCanChooseDirectories:", can?1:0);
    }   
    
    public boolean getResolvesAliases(){
        return getI("resolvesAliases")!=0;
    }
    
    public void setResolvesAliases(boolean resolves){
        set("setResolvesAliases:", resolves?1:0);
    }
    
    /**
     * Sets the directory that the dialog displays.
     * @param dir 
     */
    public void setDirectory(final String dir){
        dispatch_sync(new Runnable(){
            @Override
            public void run() {
                Proxy url = getClient().sendProxy("NSURL", "fileURLWithPath:isDirectory:", dir, 1);
                peer.send("setDirectoryURL:", url.getPeer());
            }
        });
    }
    
    /**
     * Unsupported
     * @param file 
     */
    public void setFile(String file){
        throw new UnsupportedOperationException("setFile() notimplemented");
    }
    
    /**
     * Unsupported
     * @param mode 
     */
    public void setMode(int mode){
        throw new UnsupportedOperationException("Can't set mode after initialization");
    }
    
    
    public void addChoosableFilter(String fileExtension, String description) {
    	chooseableFilters.add(new Pair<String, String>(fileExtension, description));
    }
    
    public void addChoosableFilter(FileFilter filter) {
    	if (filter instanceof HvTaggedFileFilter) {
    		HvTaggedFileFilter taggedFilter = (HvTaggedFileFilter) filter;

    		FilterInfo info = new FilterInfo(taggedFilter.getTag(),
    				taggedFilter.acceptor().getDescription(), 
    				taggedFilter.acceptor().getStringExtensions());
    		betterFilters.add(info);
    	}
    }
    
    @Override
    protected void finalize() throws Throwable {
        super.finalize(); 
    }
    
    public void setVisible(boolean visible){
        dispatch_sync(new Runnable(){
            @Override
            public void run() {
                peer.send("runModal");
            }           
        });
    }

    private NSRect makeRect(double x, double y, double w, double h) {
    	return new NSRect(new NSPoint(x, y), new NSSize(w, h));
    }
    
    @Msg(selector="cbSelectFormat:", signature="v@:@")
    public void cbSelectFormat(final Proxy sender) {
    	dispatch_sync(new Runnable() {
    		@Override
    		public void run() {
//    	        NSInteger      selectedItemIndex      = [button indexOfSelectedItem];
    	    	int index = sender.sendInt("indexOfSelectedItem");

    	    	if (index >= 0 && index < betterFilters.size()) {
    	    		setAllowedFileTypesImpl(betterFilters
    	    				.get(index).getExtensions());    	    		
    	    		selectedFilterIndex = index;
    	    		return;
    	    	}
    	    	
    	    	if(index >= 0 && index < chooseableFilters.size()) {
//    	        	System.out.println("User has choosen index " + index + " => " + chooseableFilters.get(index).getKey());    		

//    	            NSString      *nameFieldString        = [[self savePanel] nameFieldStringValue];
//    	            NSString      *trimmedNameFieldString = [nameFieldString stringByDeletingPathExtension];
//    	            NSString *nameFieldStringWithExt = [NSString stringWithFormat:@"%@.%@", trimmedNameFieldString, extension];
//    	            [[self savePanel] setNameFieldStringValue:nameFieldStringWithExt];
//    	            [[self savePanel] setAllowedFileTypes:@[extension]];
    	 
    	        	String extension = extractFileExtension(chooseableFilters.get(index).getKey());
//    	        	String oldName = peer.sendString("nameFieldStringValue");
//   	        	int pointIndex = oldName.lastIndexOf(".");
//    	        	String nameFileStringWithExt = pointIndex != -1 ? oldName.substring(0, pointIndex) : oldName;
//    	        	nameFileStringWithExt = nameFileStringWithExt + "." + extension;
    	    		List<String> newExtension = Arrays.asList(extension);
//    	    		peer.send("setNameFieldStringValue:", nameFileStringWithExt);
    	    		setAllowedFileTypesImpl(newExtension);
    	    		
    	    		selectedFilterIndex = index;
//    	    	} else {
//    	    		System.out.println("User has choosen invalid index " + index + "!");
    	    	}    			
    		}
    	});
    }
    
    private void installAccessoryView() {
    	final OSXNativeFileDialog me = this;
    	
        dispatch_sync(new Runnable(){

            @Override
            public void run() {
//              NSArray *buttonItems   = [NSArray arrayWithObjects:@"JPEG (*.jpg)", @"GIF (*.gif)", @"PNG (*.png)", nil];
                Proxy buttonItems = getClient().sendProxy("NSMutableArray", "arrayWithCapacity:", chooseableFilters.size());
  
                if (betterFilters.size() > 0) {
                	for (FilterInfo entry : betterFilters) {
                		String tag = entry.getTag();
                		String extension = entry.getDescription();
	                	buttonItems.send("addObject:", tag + " " + extension);
                	}
                } else {
	                for (Pair<String,String> entry: chooseableFilters){                 
	                	buttonItems.send("addObject:", entry.getValue());
	                }
                }
                
//              NSView  *accessoryView = [[NSView alloc] initWithFrame:NSMakeRect(0.0, 0.0, 200, 32.0)];   
                Proxy accessoryView = getClient().sendProxy("NSView", "alloc");
                accessoryView = accessoryView.sendProxy("initWithFrame:", makeRect(0.0, 0.0, 200.0, 32.0));
                
//              NSTextField *label = [[NSTextField alloc] initWithFrame:NSMakeRect(0, 0, 60, 22)];
                Proxy label = getClient().sendProxy("NSTextField", "alloc");
                label = label.sendProxy("initWithFrame:", makeRect(0.0, 0.0, 60.0, 22.0));

                label.send("setEditable:", 0);
                label.send("setBordered:", 0);
                label.send("setBezeled:", 0);
                label.send("setDrawsBackground:", 0);
                label.send("setStringValue:", "Format:");
 
//              NSPopUpButton *popupButton = [[NSPopUpButton alloc] initWithFrame:NSMakeRect(50.0, 2, 140, 22.0) pullsDown:NO];
                Proxy popupButton = getClient().sendProxy("NSPopUpButton", "alloc");
                popupButton = popupButton.sendProxy("initWithFrame:pullsDown:", makeRect(50.0, 2.0, 140.0, 22.0), 0);
//   [popupButton addItemsWithTitles:buttonItems];
                popupButton.send("addItemsWithTitles:", buttonItems);
                
//                String title = popupButton.sendString("itemTitleAtIndex:", 2);
                
//                [popupButton setAction:@selector(selectFormat:)]; 

                popupButton.send("setTarget:", me);
                popupButton.send("setAction:", sel("cbSelectFormat:"));
                popupButton.send("setAutoenablesItems:", 0);
                
//                [accessoryView addSubview:label];
                accessoryView.send("addSubview:", label);
//                [accessoryView addSubview:popupButton];
                accessoryView.send("addSubview:", popupButton);

//                [[self savePanel] setAccessoryView:accessoryView];                
                peer.send("setAccessoryView:", accessoryView);      
            }
        });
    }
    
    private String extractFileExtension(String description) {
		if(description.startsWith("A-")) {
			int index = description.indexOf("*.");
			if(index != -1) {
				description = description.substring(index + 2);
				description = description.substring(0, description.length()-1);
			}
		}
		return description;
    }
    
    /**
     * Zeigt den modalen FileDialog</br>
     * <p>Es wird zus&auml;tzlich eine "AccessoryView" verwendet,
     * um die diversen vom Anwender w&auml;hlbaren Fileextensions
     * (choosableFileFilters) auch als solche f&uuml;r den 
     * Anwender darzustellen.</p>
     * 
     * @return answers 1 if user did a selection, 0 otherwise
     */
    public int runModal() {
    	List<String> allTypes = new ArrayList<String>();
    	if (betterFilters.size() > 0) {
    		for (FilterInfo entry : betterFilters) {
    			allTypes.addAll(entry.getExtensions());
    		}
    	} else {
	    	for (Pair<String, String> entry : chooseableFilters) {
	    		String key = extractFileExtension(entry.getKey());
	 			allTypes.add(key);
			}
    	}
    	
    	if (!allTypes.isEmpty()) {
        	setAllowedFileTypes(allTypes);
        	installAccessoryView();    		
    	}
  
    	return getI("runModal");
    }
    
    public int selectedFilterIndex() {
    	return selectedFilterIndex;
    }
    
    private class FilterInfo {
    	private String tag;
    	private List<String> extensions;
    	private String description;
    	
    	public FilterInfo(String tag, String description, List<String>extensions) {
    		this.setTag(tag);
    		this.setDescription(description);
    		this.setExtensions(new ArrayList<String>(extensions));
    	}

		public String getTag() {
			return tag;
		}

		public void setTag(String tag) {
			this.tag = tag;
		}

		public List<String> getExtensions() {
			return extensions;
		}

		public void setExtensions(List<String> extensions) {
			this.extensions = extensions;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
    }
}
