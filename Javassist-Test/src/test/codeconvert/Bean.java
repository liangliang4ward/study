package test.codeconvert;

public class Bean
{
    private String m_a;
    private String m_b;
    
    public Bean() {}
    
    public Bean(String a, String b) {
        m_a = a;
        m_b = b;
    }
    
    public String getA() {
        return m_a;
    }
    public String getB() {
        return m_b;
    }
    public void setA(String string) {
        m_a = string;
    }
    public void setB(String string) {
        m_b = string;
    }
}
