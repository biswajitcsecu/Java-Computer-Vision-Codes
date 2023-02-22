
import vtk.vtkNativeLibrary;
import vtk.vtkRenderWindow;
import vtk.vtkRenderWindowInteractor;
import vtk.vtkRenderer;
import vtk.vtkActor;
import vtk.vtkNamedColors;
import vtk.vtkPolyDataMapper;
import vtk.vtkPLYReader;

public class ReadPLY 
{

  // Load VTK library 
  static  {vtkNativeLibrary.LoadAllNativeLibraries();} 

  public static void main(String args[]) 
  {

    String inputFilename ="/home/picox/Projects/Eclipse/ReadPLY/src/src.ply";

    vtkPLYReader reader = new vtkPLYReader();
    reader.SetFileName(inputFilename);
    reader.Update();

    vtkNamedColors Color = new vtkNamedColors(); 

    //Renderer Background Color
    double BgColor[] = new double[4];

    //Change Color Name to Use your own Color for Renderer Background
    Color.GetColor("Black",BgColor);

    // Create a mapper and actor
    vtkPolyDataMapper mapper = new vtkPolyDataMapper();
    mapper.SetInputConnection(reader.GetOutputPort());

    vtkActor actor = new vtkActor();
    actor.SetMapper(mapper);

    // Create the renderer, render window and interactor.
    vtkRenderer ren = new vtkRenderer();
    vtkRenderWindow renWin = new vtkRenderWindow();
    renWin.AddRenderer(ren);
    vtkRenderWindowInteractor iren = new vtkRenderWindowInteractor();
    iren.SetRenderWindow(renWin);

    ren.AddActor(actor);
    ren.SetBackground(BgColor);

    renWin.SetSize(1000,800);
    renWin.Render();

    iren.Initialize();
    iren.Start();

  }
}