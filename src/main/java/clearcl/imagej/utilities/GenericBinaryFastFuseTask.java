package clearcl.imagej.utilities;

import clearcl.ClearCLContext;
import clearcl.ClearCLImage;
import clearcl.ClearCLKernel;
import fastfuse.FastFusionEngine;
import fastfuse.FastFusionEngineInterface;
import fastfuse.tasks.TaskBase;

import fastfuse.tasks.TaskHelper;
import org.apache.commons.lang3.tuple.MutablePair;

import java.io.IOException;
import java.util.Map;

/**
 * Author: Robert Haase (http://haesleinhuepf.net) at MPI CBG (http://mpi-cbg.de)
 * February 2018
 */
public class GenericBinaryFastFuseTask extends TaskBase
{
  ClearCLContext mContext;

  String mKernelName;
  Map<String, Object> mParameterMap;




  public GenericBinaryFastFuseTask(FastFusionEngine pFastFusionEngine,
                                   Class pAnchorClass,
                                   String pProgramFilename,
                                   String pKernelName) throws
                                                           IOException
  {
    super();
    setupProgram(pAnchorClass, pProgramFilename);
    mKernelName = pKernelName;
    mContext = pFastFusionEngine.getContext();
  }

  public void setParameterMap(Map<String, Object> pParameterMap)
  {
    mParameterMap = pParameterMap;
  }

  @Override public boolean enqueue(FastFusionEngineInterface pFastFusionEngine,
                                   boolean pWaitToFinish)
  {
    System.out.println("available images are");

    ClearCLImage lSrcImage = null;
    ClearCLImage lDstImage = null;

    if (mParameterMap != null) {
      for (String key : mParameterMap.keySet()) {
        if (key == "src") {
          lSrcImage = (ClearCLImage) mParameterMap.get(key);
        } else if (key == "dst") {
          lDstImage = (ClearCLImage) mParameterMap.get(key);
        }
      }
    }

    ClearCLKernel lClearCLKernel = null;
    if (lSrcImage != null && lDstImage != null) {

      try
      {
        lClearCLKernel =
            getKernel(mContext, mKernelName,
                      TaskHelper.getOpenCLDefines(lSrcImage,
                                                  lDstImage));
      }
      catch (IOException e1)
      {
        e1.printStackTrace();
        return false;
      }

    } else
    {
      try
      {
        lClearCLKernel =
        getKernel(mContext,
                  mKernelName);
      }
      catch (IOException e1)
      {
        e1.printStackTrace();
        return false;
      }
    }

    if (lClearCLKernel != null)
    {
      lClearCLKernel.setGlobalSizes(lDstImage.getDimensions());

      if (mParameterMap != null)
      {
        for (String key : mParameterMap.keySet())
        {
          lClearCLKernel.setArgument(key, mParameterMap.get(key));
        }
      }
      runKernel(lClearCLKernel, pWaitToFinish);
    }

    return true;
  }
}
