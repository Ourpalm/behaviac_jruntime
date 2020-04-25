package org.gof.behaviac.behaviortree;

public class BsonDeserizer {
    public class BsonTypes {
    	public static final int BT_None = 0;
        public static final int BT_Double = 1;
        public static final int BT_String = 2;
        public static final int BT_Object = 3;
        public static final int BT_Array = 4;
        public static final int BT_Binary = 5;
        public static final int BT_Undefined = 6;
        public static final int BT_ObjectId = 7;
        public static final int BT_Boolean = 8;
        public static final int BT_DateTime = 9;
        public static final int BT_Null = 10;
        public static final int BT_Regex = 11;
        public static final int BT_Reference = 12;
        public static final int BT_Code = 13;
        public static final int BT_Symbol = 14;
        public static final int BT_ScopedCode = 15;
        public static final int BT_Int32 = 16;
        public static final int BT_Timestamp = 17;
        public static final int BT_Int64 = 18;
        public static final int BT_Float = 19;
        public static final int BT_Element = 20;
        public static final int BT_Set = 21;
        public static final int BT_BehaviorElement = 22;
        public static final int BT_PropertiesElement = 23;
        public static final int BT_ParsElement = 24;
        public static final int BT_ParElement = 25;
        public static final int BT_NodeElement = 26;
        public static final int BT_AttachmentsElement = 27;
        public static final int BT_AttachmentElement = 28;
        public static final int BT_AgentsElement = 29;
        public static final int BT_AgentElement = 30;
        public static final int BT_PropertyElement = 31;
        public static final int BT_MethodsElement = 32;
        public static final int BT_MethodElement = 33;
        public static final int BT_Custom = 34;
        public static final int BT_ParameterElement = 35;
    }

    public boolean init(byte[] pBuffer)
    {
        try
        {
            m_pBuffer = pBuffer;

            if (m_pBuffer != null && m_pBuffer.Length > 0)
            {
                m_BinaryReader = new BinaryReader(new MemoryStream(m_pBuffer));

                if (this.OpenDocument())
                {
                    return true;
                }
            }
        }
        catch (Exception e)
        {
            Debug.Check(false, e.Message);
        }

        Debug.Check(false);
        return false;
    }

    private int GetCurrentIndex()
    {
        Debug.Check(this.m_BinaryReader != null);
        return (int)this.m_BinaryReader.BaseStream.Position;
    }

    public bool OpenDocument()
    {
        int head = this.GetCurrentIndex();
        int size = this.ReadInt32();
        int end = head + size - 1;

        if (this.m_pBuffer[end] == 0)
        {
            return true;
        }
        else
        {
            Debug.Check(false);
            return false;
        }
    }

    //if ReadType has been called as a 'peek', use CloseDocumente(false)
    //usually, after a loop, use CloseDocumente(false) as that loop usually terminates with e peek ReadType
    public void CloseDocument(bool bEatEod /*= false*/)
    {
        int endLast = this.GetCurrentIndex();

        if (bEatEod)
        {
            this.m_BinaryReader.BaseStream.Position++;
        }
        else
        {
            endLast--;
        }

        Debug.Check(this.m_pBuffer[endLast] == 0);
    }

    public BsonTypes ReadType()
    {
        byte b = m_BinaryReader.ReadByte();

        return (BsonTypes)b;
    }

    public int ReadInt32()
    {
        int i = m_BinaryReader.ReadInt32();

#if LITTLE_ENDIAN_ONLY
        Debug.Check(BitConverter.IsLittleEndian);
        return i;
#else

        if (BitConverter.IsLittleEndian)
        {
            return i;
        }
        else
        {
            byte[] bytes = BitConverter.GetBytes(i);
            i = (bytes[0] << 24 | bytes[1] << 16 | bytes[2] << 8 | bytes[3]);

            return i;
        }

#endif//LITTLE_ENDIAN_ONLY
    }

    public UInt16 ReadUInt16()
    {
        ushort us = m_BinaryReader.ReadUInt16();

#if LITTLE_ENDIAN_ONLY
        Debug.Check(BitConverter.IsLittleEndian);
        return us;
#else

        if (BitConverter.IsLittleEndian)
        {
            return us;
        }
        else
        {
            byte[] bytes = BitConverter.GetBytes(us);
            us = (ushort)(bytes[0] << 8 | bytes[1]);

            return us;
        }

#endif//LITTLE_ENDIAN_ONLY
    }

    public float ReadFloat()
    {
        float f = m_BinaryReader.ReadSingle();

#if LITTLE_ENDIAN_ONLY
        Debug.Check(BitConverter.IsLittleEndian);
        return f;
#else

        if (BitConverter.IsLittleEndian)
        {
            return f;
        }
        else
        {
            byte[] bytes = BitConverter.GetBytes(f);
            Array.Reverse(bytes);
            f = BitConverter.ToSingle(bytes, 0);

            return f;
        }

#endif//LITTLE_ENDIAN_ONLY
    }

    public bool ReadBool()
    {
        byte b = m_BinaryReader.ReadByte();

        return (b != 0) ? true : false;
    }

    public string ReadString()
    {
#if USE_STRING_COUNT_HEAD
        UInt16 count = ReadUInt16();
        byte[] bytes = m_BinaryReader.ReadBytes(count);

        // The exporter uses UTF8 to export strings, so the same encoding type is used here.
        string str = System.Text.Encoding.UTF8.GetString(bytes, 0, count - 1);

        Debug.Check(this.m_pBuffer[this.GetCurrentIndex() - 1] == 0);
        return str;
#else
        List<byte> bytes = new List<byte>();

        while (true)
        {
            byte b = m_BinaryReader.ReadByte();

            if (b == 0)
            {
                break;
            }

            bytes.Add(b);
        }

        // The exporter uses UTF8 to export strings, so the same encoding type is used here.
        string str = System.Text.Encoding.UTF8.GetString(bytes.ToArray());

        return str;
#endif
    }

    public bool eod()
    {
        byte c = this.m_pBuffer[this.GetCurrentIndex()];
        return (c == 0);
    }

    private byte[] m_pBuffer = null;
    private BinaryReader m_BinaryReader = null;
}