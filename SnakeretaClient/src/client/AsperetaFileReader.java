package client;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AsperetaFileReader {

	public static void load(File file, World world) throws IOException {
		//Image img = new Image(new ByteArrayInputStream(buffer));
		//DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
		Path path = Paths.get(file.getPath());
		byte[] bytes =  Files.readAllBytes(path);
		byte offset = bytes[bytes.length - 2];
		//int fileType = in.readByte();
		//in.readByte();
		//int extraLength = in.readInt()+1;
		//Log.println("Type: "+fileType+", Extra Length: "+extraLength);//+";"+ApplyOffsetByte(in.readByte(), offset)+":"+ApplyOffsetByte(in.readByte(), offset)+":"+ApplyOffsetByte(in.readByte(), offset)+":"+ApplyOffsetByte(in.readByte(), offset));
		/*String extraBytes = "Extra Bytes: ";
		for(int i = 0; i < extraLength; ++i) {
			if(i!=0) extraBytes+=", ";
			extraBytes+=ApplyOffsetByte(in.readByte(), offset);
		}
		Log.println(extraBytes);
		int numberOfFrames = ApplyOffset(in.readInt(), offset);
		Log.println("NumberOfFrames: "+numberOfFrames);
		*/
		/*for(int i = 0; i < numberOfFrames; ++i) {
			int frameId = ApplyOffset(in.readInt(), offset);
			// always 1 as far as i have seen
			byte unknownByte = ApplyOffsetByte(in.readByte(), offset);
			int x = ApplyOffset(in.readInt(), offset);
			int y = ApplyOffset(in.readInt(), offset);
			int width = ApplyOffset(in.readInt(), offset);
			int height = ApplyOffset(in.readInt(), offset);
		
			Log.println("Id: "+frameId+" U: "+unknownByte+" X: "+x+" Y: "+y+" W: "+width+" H: "+height);
		}
		int unknown = ApplyOffset(in.readInt(), offset);
		Log.println("U: "+unknown);
	
		//int length = (int)(reader.BaseStream.Length - reader.BaseStream.Position);
		byte[] buffer = new byte[10000];
		Log.println("Read Buffer: "+in.read(buffer));
		Log.println(buffer.length);
		//byte[] data = new byte[RealSize(buffer.Length, 0x315)];
		//for (int k = 0; k < buffer.Length; k++)
		//{
		//data[k - (k / 790)] = ApplyOffsetByte(buffer[k], offset);
		//}
	
		//string ext = ".bmp";
		//if (unknown == 36) ext = ".wav";
	
		//File.WriteAllBytes(Path.GetFileName(file) + ext, data);
		*/
		//in.close();
	}
	public static int ApplyOffset(int data, int offset)
	{
		return (data - offset);
	}

	public static byte ApplyOffsetByte(byte data, int offset)
	{
		if (offset > data)
		{
			data = (byte)(data + (0x100 - offset));
			return data;
		}
		data = (byte)(data - offset);
		return data;
	}

	/*public static int RealSize(int datasize, int chunksize)
	{
		return (datasize - (datasize / (chunksize + 1)));
	}

	static void Main(string[] args)
	{
		foreach (string file in Directory.GetFiles(@".", "*.adf"))
		{
			byte[] bytes = File.ReadAllBytes(file);
		
			// so the last byte happens to be twice the "offset" number.
			// but that doesn't matter because the second to last byte is *always?* the offset number anyways!
			byte offset = bytes[bytes.Length - 2];
		
			BinaryReader reader = new BinaryReader(new MemoryStream(bytes));
			StreamWriter writer = new StreamWriter(Path.GetFileName(file) + "-header.txt");
		
			byte fileType = reader.ReadByte();
			int extraLength = reader.ReadInt32() + 1;
			writer.WriteLine("Type: {0}, Extra Length: {1}", fileType, extraLength);
			// not sure what these extra bytes are
			writer.Write("Extra Bytes: ");
			for (int i = 0; i < extraLength; i++) writer.Write("{0,4}", ApplyOffsetByte(reader.ReadByte(), offset));
			writer.WriteLine();
			int numberOfFrames = ApplyOffset(reader.ReadInt32(), offset);
			writer.WriteLine("NumberOfFrames: {0}", numberOfFrames);
		
			for (int i = 0; i < numberOfFrames; i++)
			{
				int frameId = ApplyOffset(reader.ReadInt32(), offset);
				// always 1 as far as i have seen
				byte unknownByte = ApplyOffsetByte(reader.ReadByte(), offset);
				int x = ApplyOffset(reader.ReadInt32(), offset);
				int y = ApplyOffset(reader.ReadInt32(), offset);
				int width = ApplyOffset(reader.ReadInt32(), offset);
				int height = ApplyOffset(reader.ReadInt32(), offset);
			
				writer.WriteLine("Id: {0,4} U: {1,4} X: {2,4} Y: {3,4} W: {4,4} H: {5,4}", frameId, unknownByte, x, y, width, height);
			}
	
			// not sure what this is, 36 = wav though i think
			int unknown = ApplyOffset(reader.ReadInt32(), offset);
			writer.WriteLine("U: {0}", unknown);
		
			writer.Close();
		
			int length = (int)(reader.BaseStream.Length - reader.BaseStream.Position);
			byte[] buffer = reader.ReadBytes(length);
			byte[] data = new byte[RealSize(buffer.Length, 0x315)];
			for (int k = 0; k < buffer.Length; k++)
			{
			data[k - (k / 790)] = ApplyOffsetByte(buffer[k], offset);
			}
		
			string ext = ".bmp";
			if (unknown == 36) ext = ".wav";
		
			File.WriteAllBytes(Path.GetFileName(file) + ext, data);
		}
	}*/
	
	
	
	
	
	
	
}
