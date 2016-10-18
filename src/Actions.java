import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxMorphing;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

public class Actions extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static mxGraph graph = new mxGraph();
	protected static HashMap m = new HashMap();
	private static mxGraphComponent graphComponent;
	private JButton evaluation;
	private JButton restart;
	private JButton changeAT;
	private JButton undoButton = new JButton("Restore");
	private  mxIGraphLayout layout;
    private static int selectedCellId = 0;

	
	
	

	public static HashMap getM() {
		return m;
	}

	public static mxGraph getGraph() {
		return graph;
	}
	
	

	public Actions(ArrayList<Argument> argArray,ArrayList<Relation> relArray){
		super("Argumentation Framework");
		initGUI(argArray, relArray);
	}
	

	
	

	public void initGUI(ArrayList<Argument> argArray,ArrayList<Relation> relArray) {
		setSize(1600, 1000);
		setLocationRelativeTo(null);
		graphComponent = new mxGraphComponent(graph);
		graphComponent.setBounds(5, 5, 1319, 1000);
		layout = new mxCircleLayout(graph);
		getContentPane().setLayout(null);
		graphComponent.setPreferredSize(new Dimension(1200, 1000));
		getContentPane().add(graphComponent);
		graph.setCellsDisconnectable(false);
        graph.setCellsEditable(false);
        graph.setCellsResizable(false);
        graph.setDropEnabled(false);
		
		for(int i = 0; i<argArray.size();i++){
			Argument argument = argArray.get(i);
			String summary = argument.getSummary();
			String activation = String.valueOf(argument.getActivity());
			String nodeInfo = summary+"\r\n"+activation;
			String argId = String.valueOf(argument.getArgId());
			AddNode.addNode(nodeInfo,i,i,argId);
		}
		
		
		
		AddAttackLine.addLine(argArray, relArray);
				
        graphComponent.getGraphControl().addMouseListener(new MouseAdapter()
		{
		
			public void mouseReleased(MouseEvent e)
			{
				graphComponent.getCellAt(e.getX(), e.getY());		
			}
		});
        
        
         Object cell = graph.getSelectionCell();
         if (cell == null
					|| graph.getModel().getChildCount(cell) == 0)
			{
				cell = graph.getDefaultParent();
			}
         graph.getModel().beginUpdate();
         try
			{
				layout.execute(cell);
			}
         finally
			{
				mxMorphing morph = new mxMorphing(graphComponent, 1,
						1.2, 40);

				morph.addListener(mxEvent.DONE, new mxIEventListener()
				{

					public void invoke(Object sender, mxEventObject evt)
					{
						graph.getModel().endUpdate();
					}

				});

				morph.startAnimation();
			}
         
           
 		AddUnderCutLine.addUnderCutLine(argArray, relArray);

 		graph.setCellsMovable(false);
        graph.setEdgeLabelsMovable(false);
        graph.setAllowDanglingEdges(false);
        graph.setSplitEnabled(false);
        graphComponent.setConnectable(false);
        
        
        evaluation = new JButton("Evaluate");
        evaluation.setBounds(1373, 370, 168, 50);
        evaluation.setFont(new Font("Arial", Font.PLAIN, 20));
        evaluation.setPreferredSize(new Dimension(168, 50));
        
        evaluation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				ArrayList<Argument> argArrayCopy = copyArgArrayList(argArray);
				ArrayList<Relation> relArrayCopy =  copyRelArrayList(relArray);
				ArrayList<Argument> soList = copyArgArrayList(argArray);
				 
				String newSummary = null;
				mxCell result = null;
				
				mxStylesheet stylesheet = graph.getStylesheet();
				Hashtable<String, Object> style = new Hashtable<String,Object>();
				style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
				style.put(mxConstants.STYLE_OPACITY, 50);
				style.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
				style.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_BOTTOM);
				style.put(mxConstants.STYLE_FILLCOLOR, "#ADF1D2");
				style.put(mxConstants.STYLE_FONTCOLOR, "#774400");
				style.put(mxConstants.STYLE_FONTSIZE, 15);
				style.put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_BOLD);
				stylesheet.putCellStyle("winnerStyle", style);
				
				
				
				mxStylesheet stylesheetUpdate = graph.getStylesheet();
				Hashtable<String, Object> styleUpdate = new Hashtable<String,Object>();
				styleUpdate.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
				styleUpdate.put(mxConstants.STYLE_OPACITY, 50);
				styleUpdate.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
				styleUpdate.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_BOTTOM);
				styleUpdate.put(mxConstants.STYLE_FILLCOLOR, "#FFFFFF");
				styleUpdate.put(mxConstants.STYLE_FONTCOLOR, "#D63E3E");
				styleUpdate.put(mxConstants.STYLE_FONTSIZE, 15);
				stylesheetUpdate.putCellStyle("updateStyle", styleUpdate);
					
						
				
				ArrayList<Argument> solution = Framework.evaluate("POE", 0, argArrayCopy, relArrayCopy,soList);			   
					
				
				
				for (int i = 0; i < solution.size(); i++) {
					int argId =  solution.get(i).getArgId();
					String summary = solution.get(i).getSummary();
					//System.out.println("Conclusion: argID: "+argId+" summary: "+summary+" activation: "+solution.get(i).getActivity());
					result= (mxCell) ((mxGraphModel)graph.getModel()).getCell(String.valueOf(solution.get(i).getArgId()+1));
					newSummary = solution.get(i).getSummary()+"\r\n"+round(solution.get(i).getActivity(),2);
					graph.getModel().setValue(result, newSummary);
					graph.getModel().setStyle(result, "updateStyle");
				}
				mxCell conclusion = null;
				if (solution.get(0).getActivity()>solution.get(1).getActivity()){
					conclusion= (mxCell) ((mxGraphModel)graph.getModel()).getCell(String.valueOf(solution.get(0).getArgId()+1));
				}else if (solution.get(0).getActivity()>solution.get(1).getActivity()) {
					conclusion= (mxCell) ((mxGraphModel)graph.getModel()).getCell(String.valueOf(solution.get(1).getArgId()+1));
				}
				graph.getModel().setStyle(conclusion, "winnerStyle");
			    evaluation.setEnabled(false);	
			    undoButton.setEnabled(true);
			    changeAT.setEnabled(false);
			}
		});
        
      
        
//        undoButton.setFont(new Font("Arial", Font.PLAIN, 20));
//        getContentPane().add(undoButton);
//        undoButton.setPreferredSize(new Dimension(168, 50));
//        undoButton.setEnabled(false);
//        undoButton.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				((mxGraphModel)graph.getModel()).clear();
//				graph.setCellsMovable(true);
//		        graph.setEdgeLabelsMovable(true);
//		        graph.setAllowDanglingEdges(true);
//		        graph.setSplitEnabled(true);
//		        graphComponent.setConnectable(true);
//				Actions newAction = new Actions(argArray,relArray);
//				Actions.this.dispose();
//				newAction.setVisible(true);
//				evaluation.setEnabled(true);
//				undoButton.setEnabled(false);
//			}
//		});
//        
        
        
        restart = new JButton("Restart");
        restart.setBounds(1373, 496, 168, 50);
        restart.setFont(new Font("Arial", Font.PLAIN, 20));
        restart.setPreferredSize(new Dimension(168, 50));
        restart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				((mxGraphModel)graph.getModel()).clear();
				graph.setCellsMovable(true);
		        graph.setEdgeLabelsMovable(true);
		        graph.setAllowDanglingEdges(true);
		        graph.setSplitEnabled(true);
		        graphComponent.setConnectable(true);
				Actions.this.dispose();
				Import imports = new Import();	
				imports.setVisible(true);
			}
		});
        
        
        
      
        changeAT = new JButton("New value");
        changeAT.setBounds(1373, 433, 168, 50);
        changeAT.setFont(new Font("Arial", Font.PLAIN, 20));
        changeAT.setPreferredSize(new Dimension(168, 50));
        

        graph.getSelectionModel().addListener(mxEvent.CHANGE, new mxIEventListener() {
			
			@Override
			public void invoke(Object arg0, mxEventObject arg1) {
				selectedCellId = Integer.parseInt(((mxCell)graph.getSelectionCell()).getId())-1;
			}
		});
        changeAT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Argument selectedArg = Framework.getArg(selectedCellId, argArray);
				if(selectedArg != null){
					String v1 = JOptionPane.showInputDialog("Set the activation");
					selectedArg.setActivity(Double.parseDouble(v1));
					System.out.println(selectedArg.getActivity());
				}else{
					int selectedRelId = selectedCellId-1000+1;
					Relation selectedRel = Framework.getRel(selectedRelId, relArray);
					String v2 = JOptionPane.showInputDialog("Set the weight");
					selectedRel.setWeight(Double.parseDouble(v2));
					System.out.println(selectedRel.getWeight());
				}
			}
		});
        
              
        
        
        getContentPane().add(evaluation);
        getContentPane().add(changeAT);
        getContentPane().add(restart);

        
	}

	

	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	
	public static ArrayList<Argument> copyArgArrayList(ArrayList<Argument> origin) {
		ArrayList<Argument> copy = new ArrayList<Argument>();
		for(int i = 0; i<origin.size();i++){
			Argument temp = origin.get(i);
			copy.add(temp);
		}
		return copy;
	}
	
	public static ArrayList<Relation> copyRelArrayList(ArrayList<Relation> origin) {
		ArrayList<Relation> copy = new ArrayList<Relation>();
		for(int i = 0; i<origin.size();i++){
			Relation temp = origin.get(i);
			copy.add(temp);
		}
		return copy;
	}
	
}

