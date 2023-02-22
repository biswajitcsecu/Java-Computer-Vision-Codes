import vtk.vtkNativeLibrary;
import vtk.vtkActor;
import vtk.vtkSphereSource;
import vtk.vtkMultiBlockDataSet;
import vtk.vtkCompositePolyDataMapper2;
import vtk.vtkCompositeDataDisplayAttributes;
import vtk.vtkNamedColors;
import vtk.vtkRenderWindow;
import vtk.vtkRenderWindowInteractor;
import vtk.vtkRenderer;



class CompositePolyDataMapper  {  

  public static void run() {
    vtkNamedColors Color = new vtkNamedColors(); 
    //For Actor Color-------------------------------------
    double ActorColor[] = new double[4];
    //For Renderer Background Color------------------------
    double BgColor[] = new double[4];

    // Change Actor Color-----------------------------------
    Color.GetColor("Red",ActorColor);
    //Change Color for Renderer Background------------------
    Color.GetColor("Black",BgColor);
    
    vtkSphereSource Sphere1 = new vtkSphereSource();
    Sphere1.SetRadius(3);
    Sphere1.SetCenter(0, 0, 0);
    Sphere1.Update();

    vtkSphereSource Sphere2 = new vtkSphereSource();
    Sphere2.SetRadius(2);
    Sphere2.SetCenter(2, 0, 0);
    Sphere2.Update();

    vtkMultiBlockDataSet MBDS = new vtkMultiBlockDataSet();
    MBDS.SetNumberOfBlocks(3);
    MBDS.SetBlock(0, Sphere1.GetOutput());

    //NULL blocks------------------------------
    MBDS.SetBlock(2, Sphere2.GetOutput());
    vtkCompositePolyDataMapper2 Mapper = new vtkCompositePolyDataMapper2();
    Mapper.SetInputDataObject(MBDS);


    vtkCompositeDataDisplayAttributes CDSA = new vtkCompositeDataDisplayAttributes();
    Mapper.SetCompositeDataDisplayAttributes(CDSA);    
    Mapper.SetBlockColor(3, ActorColor);
    vtkActor Actor = new vtkActor();
    Actor.SetMapper(Mapper);

    // Create the renderer, render window and interactor--------
    vtkRenderer ren = new vtkRenderer();
    vtkRenderWindow renWin = new vtkRenderWindow();
    renWin.AddRenderer(ren);
    vtkRenderWindowInteractor iren = new vtkRenderWindowInteractor();
    iren.SetRenderWindow(renWin);

    // Visualise the arrow--------------------------------
    ren.AddActor(Actor);
    ren.SetBackground(BgColor);
    ren.ResetCamera();

    renWin.SetSize(1100, 800);
    renWin.Render();
    iren.Initialize();
    iren.Start();

  }
} 


public class CompositePolyDataMapperApp {
	// Load VTK library -------------------------------------
	  static {vtkNativeLibrary.LoadAllNativeLibraries(); }
	  
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CompositePolyDataMapper.run();

	}

}
