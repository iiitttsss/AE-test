package com;

import java.io.FileWriter;
import java.io.IOException;

import processing.core.PApplet;
import processing.data.FloatList;

public class Perceptron
{
	public FloatList weights;
	// private float bias;
	private float mutationRate;

	public Perceptron()
	{
	}

	public Perceptron(int num)
	{
		weights = new FloatList();
		mutationRate = 1.0f / num;
		for (int i = 0; i < num; i++)
		{
			weights.append(0);
		}
		fillRandom();
	}

	/*
	 * public void setConstant(float num) { constant = num; }
	 */
	public void setMutationRate(float num)
	{
		mutationRate = num;
	}

	/*
	 * public float getConstant() { return constant; }
	 */
	public float getMutationRate()
	{
		return mutationRate;
	}

	public float calculatePerceptron(FloatList inputs)
	{
		// float sum = constant;
		// float sum = bias;
		float sum = 0;

		for (int i = 0; i < inputs.size(); i++)
		{
			sum += inputs.get(i) * weights.get(i);
		}
		sum = activationFunction(sum);
		// System.out.println(sum);
		return sum;
	}

	public float activationFunction(float value)
	{
		/*
		 * float temp; temp = 1.0 / (1 + exp(-1 * value)); return temp;
		 */
		if (value <= -3)
		{
			return 0;
		}
		else if (value >= 3)
		{
			return 1;
		}
		else
		{
			return (1 / 6.0f) * (value - 3.0f) + 1.0f;
		}
	}

	public void fillRandom()
	{
		final float change = 100;
		for (int i = 0; i < weights.size(); i++)
		{
			float setTo = (float) (Math.random() * 2 * mutationRate * change - mutationRate * change);
			weights.set(i, setTo);
			// System.out.println(setTo);
			// weights.set(i, random(-change, change));
			// System.out.println(weights.get(i));
		}
		// bias = random(-change, change);
		// bias = random(-mutationRate * change, mutationRate * change);
	}

	public void displayPerceptron()
	{
		System.out.print(mutationRate + " - ");
		for (int i = 0; i < weights.size(); i++)
		{
			System.out.print(weights.get(i) + "|");
		}
		System.out.println();
	}

	public void copyPerceptron(Perceptron from)
	{
		for (int i = 0; i < weights.size(); i++)
		{
			weights.set(i, from.weights.get(i));
		}
		// weights = from.weights;
		// constant = from.getConstant();
		mutationRate = from.getMutationRate();
	}

	public void savePerceptron(String perceptronName)
	{
		try
		{
			FileWriter w = new FileWriter(perceptronName + ".txt");
			// w.println(constant);
			for (int i = 0; i < weights.size(); i++)
			{
				w.write(String.valueOf(weights.get(i)));
			}
			w.close();
		}
		catch (IOException e)
		{
			System.out.println("An error occurred in the savePerceptron method");
			e.printStackTrace();
		}
	}

	public void loadPerceptron(PApplet pro, String perceptronName)
	{
		String[] lines = pro.loadStrings(perceptronName + ".txt");
		for (int i = 0; i < lines.length; i++)
		{
			weights.set(i, Float.parseFloat(lines[i]));
		}
		for (int i = lines.length; i < weights.size() - lines.length; i++)
		{
			weights.set(i, 0);
		}
	}

	public float[] mutateByError(float error, float cmRate)
	{
		float[] returnError = new float[weights.size()];
		for (int i = 0; i < weights.size(); i++)
		{
			// get error
			returnError[i] = error * weights.get(i);
			// correct error
			weights.set(i, weights.get(i) + (error * cmRate));
			// weights.set(i, weights.get(i) + (Math.abs(weights.get(i)) * error * cmRate));

		}
		// bias = bias * (1 + error * cmRate);
		return returnError;
	}
}
