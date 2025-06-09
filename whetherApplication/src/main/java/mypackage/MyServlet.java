package mypackage;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class MyServlet
 */
@WebServlet("/MyServlet")
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.sendRedirect("index.html");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String inputData=request.getParameter("city");
		
		System.out.println(inputData);
		
		String apiKey = "fee2dd83aa6740f3bbf78b5d9d2b0e13";
		String city = request.getParameter("city");
		String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8.toString());

		String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + encodedCity + "&appid=" + apiKey + "&units=metric";
		
		URL url= new URL(apiUrl);
		
		HttpsURLConnection connection=(HttpsURLConnection) url.openConnection();
		
		connection.setRequestMethod("GET");
		
		
		InputStream inputStream=connection.getInputStream();
		InputStreamReader reader= new InputStreamReader(inputStream);
		
		System.out.println(reader);
		
		Scanner sc= new Scanner(reader);
		StringBuilder responseContent= new StringBuilder();
		
		while (sc.hasNext()) {
			 responseContent.append(sc.nextLine());
			 
		}
		
		sc.close();
		System.out.println(responseContent);
		
		
		Gson gson= new Gson();
		JsonObject jsonObject=gson.fromJson(responseContent.toString(), JsonObject.class);
		
		System.out.println(jsonObject);
		
		//Date &Time
		
		long dateTimeStamp=jsonObject.get("dt").getAsLong()*1000;
		String date= new Date(dateTimeStamp).toString();
		
		//Temprature
		
		double temperatureKelvin=jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
		int temperatureCelsius=(int)(temperatureKelvin-273.15);
		
		//himidity
		
		int humidity=jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
		
		//wind speed
		
		double windSpeed=jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
		
		//whether Condition
		
		String weatherCondition=jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
		
		
		 // Set the data as request attributes (for sending to the jsp page)
        request.setAttribute("date", date);
        request.setAttribute("city", city);
        request.setAttribute("temperature", temperatureCelsius);
        request.setAttribute("weatherCondition", weatherCondition); 
        request.setAttribute("humidity", humidity);    
        request.setAttribute("windSpeed", windSpeed);
        request.setAttribute("weatherData", responseContent.toString());
        
        connection.disconnect();
		
        
        
		
		request.getRequestDispatcher("index.jsp").forward(request, response);
		
	}

}
