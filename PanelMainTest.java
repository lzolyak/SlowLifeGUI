import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.*;
import java.util.Hashtable;

public class PanelMainTest {

	//Test for the convertToInt method of the MainPanel Class
	//Testing for zero
	@Test
	public void toIntTestZero(){
		
		//instantiate our new panel
		MainPanel panel = new MainPanel(10);
		
		//holds the classes of args the method takes
		Class<?>[] classes = {int.class};
		
		try{
			//use reflection to make the private method accessible
			Method m = panel.getClass().getDeclaredMethod("convertToInt", classes);
			m.setAccessible(true);
			
			//invoke the method
			Object x = m.invoke(panel, 0);
			
			assertEquals(Integer.parseInt(x.toString()), 0);
		}
		catch(Exception e){
			fail(e.toString());
		}
		
	}
	
	//Test for the convertToInt method of the MainPanel Class
	//Testing on a large even number
		@Test
		public void toIntTestLarge(){
			
			//instantiate our new panel
			MainPanel panel = new MainPanel(10);
			
			//holds the classes of args the method takes
			Class<?>[] classes = {int.class};
			
			try{
				//use reflection to access the private method
				Method m = panel.getClass().getDeclaredMethod("convertToInt", classes);
				m.setAccessible(true);
				
				//invoke the method
				Object x = m.invoke(panel, 1200);
				
				assertEquals(Integer.parseInt(x.toString()), 1200);
			}
			catch(Exception e){
				fail(e.toString());
			}
			
		}
		
		//Test for the convertToInt method of the MainPanel Class
		//testing on a small odd number
		@Test
		public void toIntTestSmall(){
			
			//instantiate our new panel
			MainPanel panel = new MainPanel(10);
			
			//holds the classes of args the method takes
			Class<?>[] classes = {int.class};
			
			try{
				//use reflection to access the private method
				Method m = panel.getClass().getDeclaredMethod("convertToInt", classes);
				m.setAccessible(true);
				
				//invoke the method
				Object x = m.invoke(panel, 5);
				
				assertEquals(Integer.parseInt(x.toString()), 5);
				
			
			}
			catch(Exception e){
				fail(e.toString());
			}
			
		}


		/* Testing that backup still works correctly
		 * when the _backupCells array is initially empty
		 */
		@Test
		public void backupEmptyTest(){
			MainPanel panel = new MainPanel(10);
			
			
			try {
				Field f = panel.getClass().getDeclaredField("_backupCells");
				f.setAccessible(true);
				Cell[][] before = (Cell[][]) f.get(panel);
				panel.backup();
				Cell[][] after = (Cell[][]) f.get(panel);
				
				assertNotEquals(before, after);
			}
			catch(Exception e){
				fail(e.toString());
			}
		}
		
		/* Testing that backup matches the value of cells
		 * for a small board and backup
		 */
		@Test
		public void backupNotEmptyTest(){
			MainPanel panel = new MainPanel(10);
			
			
			try {
				Field f = panel.getClass().getDeclaredField("_cells");
				f.setAccessible(true);
				Cell[][] current_cells = (Cell[][]) f.get(panel);
				panel.backup();
				
				Field b_f = panel.getClass().getDeclaredField("_cells");
				b_f.setAccessible(true);
				Cell[][] current_backup = (Cell[][]) b_f.get(panel);
				
				for(int i = 0; i < 10; i++){
					for(int j = 0; j < 10; j++){
						if(current_cells[i][j].getAlive() != current_backup[i][j].getAlive()){
							fail("backup didn't match");
						}
					}
				}
			}
			catch(Exception e){
				fail(e.toString());
			}
		}
		
		/* Testing that backup matches the value of cells
		 * for a large board and backup (10K elements)
		 */
		@Test
		public void backupLargeTest(){
			MainPanel panel = new MainPanel(100);
			
			
			try {
				Field f = panel.getClass().getDeclaredField("_cells");
				f.setAccessible(true);
				Cell[][] current_cells = (Cell[][]) f.get(panel);
				panel.backup();
				
				Field b_f = panel.getClass().getDeclaredField("_cells");
				b_f.setAccessible(true);
				Cell[][] current_backup = (Cell[][]) b_f.get(panel);
				
				for(int i = 0; i < 100; i++){
					for(int j = 0; j < 100; j++){
						if(current_cells[i][j].getAlive() != current_backup[i][j].getAlive()){
							fail("backup didn't match");
						}
					}
				}
			}
			catch(Exception e){
				fail(e.toString());
			}
		}
		
		
		@Test
		public void runContinuousTest(){
		
			MainPanel panel = new MainPanel(10);
			MainPanel panel2 = new MainPanel(10);
			panel2.setCells(panel.getCells());
			
			boolean _running = true;
			int i = 0;
			while (_running) {
			    panel.backup();
			    
				Method m;
				try {
					m = panel.getClass().getDeclaredMethod("calculateNextIteration", null);
				
				m.setAccessible(true);
			    m.invoke(panel);
			    
			    if(++i > 3){
			    	_running = false;
			    }
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Cell[][] new_method = panel.getCells();
			
			_running = true;
			i = 0;
			
			try{
			Field f = panel2.getClass().getDeclaredField("_r");
			f.setAccessible(true);
			int _r = (int) f.get(panel2);
			
			Field f2 = panel2.getClass().getDeclaredField("_maxCount");
			f2.setAccessible(true);
			int _maxCount = (int) f2.get(panel2);
			
			Field f3 = panel2.getClass().getDeclaredField("_size");
			f3.setAccessible(true);
			int _size = (int) f3.get(panel2);
			
			while (_running) {
			    int origR = _r;
			    try {
				Thread.sleep(20);
			    } catch (InterruptedException iex) { }
			    for (int j=0; j < _maxCount; j++) {
			    	_r += (j % _size) % _maxCount;
				_r += _maxCount;
			    }
			    _r = origR;
			    panel2.backup();
			    
			    try {
				Method m_old = panel2.getClass().getDeclaredMethod("calculateNextIteration", null);
				
				m_old.setAccessible(true);
			    m_old.invoke(panel2);
			    
			    
			    if(++i > 3){
			    	_running = false;
			    }
			    }
			   catch(Exception e){
				   fail(e.toString());
				   }
			   }
			}
			catch(Exception e){
				fail(e.toString());
			}
			
		}
	
}
