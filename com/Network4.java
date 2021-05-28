package com;

import processing.core.PApplet;
import processing.data.FloatList;

class Network4
{
	public Layer hidden1;
	public Layer hidden2;
	public Layer hidden3;
	public Layer hidden4;
	public Layer output;

	public Network4(int inputsNum, int hiddenNum1, int hiddenNum2, int hiddenNum3, int hiddenNum4, int outputsNum,
			float mR)
	{
		BuildNetwork(inputsNum, hiddenNum1, hiddenNum2, hiddenNum3, hiddenNum4, outputsNum);
	}

	public Network4()
	{
		BuildNetwork(1, 1, 1, 1, 1, 1);
	}

	public float[] mutateByError(float[] error)
	{
		// mutateLayerByError
		return hidden1.mutateLayerByError(hidden2.mutateLayerByError(
				hidden3.mutateLayerByError(hidden4.mutateLayerByError(output.mutateLayerByError(error)))));
	}

	public void BuildNetwork(int inputsNum, int hiddenNum1, int hiddenNum2, int hiddenNum3, int hiddenNum4,
			int outputsNum)
	{
		hidden1 = new Layer(hiddenNum1, inputsNum);
		hidden2 = new Layer(hiddenNum2, hiddenNum1);
		hidden3 = new Layer(hiddenNum3, hiddenNum2);
		hidden4 = new Layer(hiddenNum4, hiddenNum3);
		output = new Layer(outputsNum, hiddenNum4);
	}

	public void displayNetwork4()
	{
		System.out.println("Network2:");
		hidden1.displayLayer();
		hidden2.displayLayer();
		hidden3.displayLayer();
		hidden4.displayLayer();
		output.displayLayer();
	}

	public FloatList calculateNetwork(FloatList input)
	{
		return output.calculateLayer(
				hidden4.calculateLayer(hidden3.calculateLayer(hidden2.calculateLayer(hidden1.calculateLayer(input)))));
	}

	public void copyNetwork(Network4 from)
	{
		// emptyNetwork();
		hidden1.copyLayer(from.hidden1);
		hidden2.copyLayer(from.hidden2);
		hidden3.copyLayer(from.hidden3);
		hidden4.copyLayer(from.hidden4);
		output.copyLayer(from.output);
	}

	public void saveNetwork(String networkName)
	{
		hidden1.saveLayer(networkName + "/hidden1");
		hidden2.saveLayer(networkName + "/hidden2");
		hidden3.saveLayer(networkName + "/hidden3");
		hidden4.saveLayer(networkName + "/hidden4");
		output.saveLayer(networkName + "/output");
	}

	public void loadNetwork(PApplet pro, String networkName)
	{
		hidden1.loadLayer(pro, networkName + "/hidden1");
		hidden2.loadLayer(pro, networkName + "/hidden2");
		hidden3.loadLayer(pro, networkName + "/hidden3");
		hidden4.loadLayer(pro, networkName + "/hidden4");
		output.loadLayer(pro, networkName + "/output");
	}
}
