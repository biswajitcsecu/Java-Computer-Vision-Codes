import vtk.vtkActor;
import vtk.vtkCylinderSource;
import vtk.vtkNamedColors;
import vtk.vtkNativeLibrary;
import vtk.vtkPolyDataMapper;
import vtk.vtkRenderer;
import vtk.vtkRenderWindow;
import vtk.vtkRenderWindowInteractor;



//class.
public class CylinderExample {
  // Load VTK library 
  static {vtkNativeLibrary.LoadAllNativeLibraries();}

  // main program
  public static void main(String args[]) {
    vtkNamedColors colors = new vtkNamedColors();

    double bkgColor[];
    bkgColor = new double[]{0.0, 0.0, 0.0, 1.0};

    // colors 
    colors.SetColor("BkgColor", bkgColor);

    
    double bkg[] = new double[4];
    colors.GetColor("BkgColor", bkg);

    //For Actor Color
    double actorColor[] = new double[4];
    colors.GetColor("Tomato", actorColor);
    
    //Renderer Background Color
    vtkCylinderSource cylinder = new vtkCylinderSource();
    cylinder.SetResolution(8);

    // The actor 
    vtkPolyDataMapper cylinderMapper = new vtkPolyDataMapper();
    cylinderMapper.SetInputConnection(cylinder.GetOutputPort());

    vtkActor cylinderActor = new vtkActor();
    cylinderActor.SetMapper(cylinderMapper);
    cylinderActor.GetProperty().SetColor(actorColor);
    cylinderActor.RotateX(30.0);
    cylinderActor.RotateY(-45.0);

    // The renderer generates 
    vtkRenderer ren = new vtkRenderer();
    ren.AddActor(cylinderActor);
    ren.ResetCamera();
    ren.GetActiveCamera().Zoom(1.5);
    ren.SetBackground(bkg);

    // The render window 
    vtkRenderWindow renWin = new vtkRenderWindow();
    renWin.AddRenderer(ren);
    renWin.SetSize(800, 700);
    renWin.SetWindowName("Cylinder");


    // The render window 
    vtkRenderWindowInteractor iren = new vtkRenderWindowInteractor();
    iren.SetRenderWindow(renWin);

    renWin.Render();

    iren.Initialize();
    iren.Start();
  }
}
