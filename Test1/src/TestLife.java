import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TestLife {
	public static void main(String[] args) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date born = sdf.parse("1990-02-22");
		
		Date now = new Date();
		
		   long diff = now.getTime() - born.getTime();

           long diffSeconds = diff / 1000 % 60;
           long diffMinutes = diff / (60 * 1000) % 60;
           long diffHours = diff / (60 * 60 * 1000) % 24;
           long diffDays = diff / (24 * 60 * 60 * 1000);

           System.out.print("两个时间相差：");
           System.out.print(diffDays + " 天, ");
           System.out.print(diffHours + " 小时, ");
           System.out.print(diffMinutes + " 分钟, ");
           System.out.print(diffSeconds + " 秒.");
	}
}
