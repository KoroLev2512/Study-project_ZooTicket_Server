package com.company;

import com.company.Objects.*;

import java.io.*;
import java.math.BigInteger;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static ArrayList<User> users = new ArrayList<>();
    static MessageDigest md;
    static ExecutorService service = Executors.newFixedThreadPool(1);
    static java.util.logging.Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    static HashMap<String, Ticket> list= new HashMap<>();
    static final String helpLog = "Доступны команды: clear, show, save, insert, update, remove_key, remove_greater, " +
            "remove_lower, execute_script, exit, replace_if_greater null, remove_all_by_discount, group_counting_by_type";
    static String fullPath = "";


    public static void obr(Command command, Response response, OutputStream output){
        logger.log(Level.INFO, "команда: " + command.getName());
        if(command.getName().equals("register")){
            AddUser((String) command.getArgs()[1], (String) command.getArgs()[2]);
            response.Add("успешная регистрация");
        }
        else {
            User user = getUser(command.getLogin(), command.getPassword());
            if (user != null) {
                switch (command.getName()) {
                    case "help" -> response.Add(helpLog);
                    case "command" -> response.Add("Введите команду \"help\" для просмотра списка команд");
                    case "clear" -> {
                        if (!list.isEmpty()) {
                            list.clear();
                            response.Add("Коллекция очищена.");
                        } else
                            response.Add("Коллекция пуста.");
                    }
                    case "show" -> {
                        response.Add("Формат вывода: ключ - значение");
                        for (Map.Entry<String, Ticket> val : list.entrySet()) {
                            response.Add(val.getKey() + " - " + val.getValue());
                        }
                    }
                    case "save" -> {
                        StringBuilder fileString = new StringBuilder();
                        for (Map.Entry<String, Ticket> val : list.entrySet()) {
                            String groupToFile = convertToStr(val.getValue(), val.getKey());
                            fileString.append(groupToFile);
                        }
                        writeFile(fullPath, fileString.toString());
                        response.Add("Файл сохранён");
                    }
                    case "insert" -> {
                        if (command.getArgs().length != 3) {
                            response.Add("Неверный ввод команды. Формат: insert null {element}");
                            break;
                        }
                        Ticket t = (Ticket) command.getArgs()[2];
                        t.setIds(user.getId());
                        list.put((String) command.getArgs()[1], t);
                        response.Add("111111");
                    }
                    case "update" -> {
                        if (command.getArgs().length != 3) {
                            response.Add("Неверный ввод команды. Формат: update id {element}");
                            break;
                        }
                        String[] keys = list.keySet().toArray(new String[0]);
                        for (String key : keys) {
                            String curName = list.get(key).getName();
                            if (curName.equals(command.getArgs()[2]) && user.getId().equals(list.get(key).getIds())) {
                                Ticket group = list.get(key);
                                Integer newId = group.makeId();
                                group.setId(newId);
                                list.put(key, group);
                                response.Add("ID элемента " + command.getArgs()[2] + " изменено на " + newId.toString());
                                break;
                            }
                        }
                    }
                    case "remove_key" -> {
                        if (command.getArgs().length != 2) {
                            response.Add("Неверный ввод команды. Формат: remove_key null");
                            break;
                        }
                        if (list.containsKey(command.getArgs()[1])) {
                            if(user.getId().equals(list.get(command.getArgs()[1]).getIds())) {
                                String nameOfRemoved = list.get(command.getArgs()[1]).getName();
                                list.remove(command.getArgs());
                                response.Add("Элемент " + nameOfRemoved + " удалён.");
                            }
                            else{
                                response.Add("пользователь не является создателем");
                            }
                        } else
                            response.Add("Элемента с ключом " + command.getArgs()[1] + " не существует");
                    }
                    case "remove_greater" -> {
                        if (command.getArgs().length != 2) {
                            response.Add("Неверный ввод команды. Формат: remove_greater {element}");
                            break;
                        }
                        removeRange((String) command.getArgs()[1], list, true);
                    }
                    case "remove_lower" -> {
                        if (command.getArgs().length != 2) {
                            response.Add("Неверный ввод команды. Формат: remove_lower {element}");
                            break;
                        }
                        removeRange((String) command.getArgs()[1], list, false);
                    }
                    case "execute_script" -> {
                        if (command.getArgs().length < 2) {
                            response.Add("Неверный ввод команды. Формат: execute_script file_name");
                            break;
                        }
                        String scripted = readFile("resources/" + command.getArgs()[1]);
                        if (scripted.equals("#fail#"))
                            break;
                        scripted = scripted.replaceAll("\r", "");
                        String[] scriptArgs = scripted.trim().split("\n");
                    }
                    case "replace_if_greater null" -> {
                        if (command.getArgs().length != 3) {
                            response.Add("Неверный ввод команды. Формат: replace_if_greater null {element}");
                            break;
                        }
                    }

                    case "remove_all_by_discount" -> {
                        if (command.getArgs().length != 2) {
                            response.Add("Неверный ввод команды. Формат: remove_all_by_discount {element}");
                            break;
                        }
                        String[] keys = list.keySet().toArray(new String[0]);
                        for (String key : keys) {
                            String curName = list.get(key).getName();
                            if (curName.contains((String) command.getArgs()[2])) {
                                String nameOfRemoved = list.get(command.getArgs()[1]).getName();
                                list.remove(command.getArgs()[1]);
                                response.Add("Элемент " + nameOfRemoved + " удалён.");
                                break;
                            }
                        }
                    }

                    case "group_counting_by_type" -> {
                        for (Map.Entry<String, List<TicketType>> item : TicketType.entrySet()) {
                            for (TicketType ticketType : item.getValue()) {
                                response.Add(ticketType.getDeclaringClass().getTypeName());
                            }
                        }
                    }
                    default -> response.Add("Команда не распознана. Повторите ввод.");
                }
            } else {
                response.Add("такого пользователя нет");
            }
        }
        byte[] b1 = Convert(response);
        System.out.println(b1.length);
        logger.log(Level.INFO, "отправлен ответ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    output.write(b1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public static  String hash(String text){
        if(md == null){
            try {
                md = MessageDigest.getInstance("SHA-224");
            }
            catch (Exception e){
                return "";
            }
        }
        String d = new BigInteger(1, md.digest(text.getBytes())).toString(16);
        while (d.length() < 32) {
            d = "0" + d;
        }
        return d;
    }

    public static void AddUser(String login, String password){

        users.add(new User(login, hash(password)));
    }

    public static User getUser(String login, String password){
        System.out.println(users.toString());
        System.out.println(new User(login, hash(password)));
        int ind = users.indexOf(new User(login, hash(password)));
        if(ind == -1){
            return null;
        }
        else{
            return users.get(ind);
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1111, 1, InetAddress.getByName("localhost"));


        logger.log(Level.INFO, "старт приложения");

        print("Введите название файла (файл должен располагаться в директории \"resources\" проекта):");
        String fileCode;

        if(args.length != 0) {
            fileCode = readFile(args[0]);

            initCollection(list, fileCode);
            logger.log(Level.INFO, "инит коллекции");
        }

        logger.log(Level.INFO, "старт приложения");



        System.out.println(serverSocket.getInetAddress().getHostAddress());

        service.execute(new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                try {
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                InputStream input = null;
                OutputStream output = null;
                try {
                    input = socket.getInputStream();
                    output = socket.getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] buff = new byte[2048];
                int b = 0;

                do {
                    logger.log(Level.INFO, "принят клиент");
                    try {
                        b = input.read(buff, 0, 2048);
                    } catch (IOException e) {
                        return;
                    }
                    Command command = (Command) Decode(buff, b);

                    Response response = new Response();


                    OutputStream outputStream = output;
                    new Thread(() -> obr(command, response, outputStream)).start();
                }
                while (!socket.isConnected() || b != -1);
            }
        });

    }

    public static byte[] Convert(Object object) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();// поток, который записывает все данные в массив байтов(локально)
            ObjectOutputStream outputStream = new ObjectOutputStream(stream);//поток, который преобразует обьект в массив байтов, и запишет его в stream
            outputStream.writeObject(object);//вызов записи
            return stream.toByteArray();//читаем из stream байты
        }
        catch (Exception e){
            e.printStackTrace();
            return new byte[0];
        }
    }

    public static Object Decode(byte[] buff, int length) {
        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(buff, 0, length);
            ObjectInputStream inputStream = new ObjectInputStream(stream);
            return inputStream.readObject();
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String readFile(String path) {
        StringBuilder file = new StringBuilder();
        try (InputStreamReader in = new InputStreamReader(new FileInputStream(path))) {
            int data = in.read();
            char ch;
            while (data != -1) {
                ch = (char) data;
                file.append(ch);
                data = in.read();
            }
        } catch (IOException e) {
            print(e.getMessage());
            return "#fail#";
        }
        return file.toString();
    }

    public static void writeFile(String path, String file) {
        try {
            FileWriter writer = new FileWriter(path);
            writer.write(file);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initCollection(HashMap<String, Ticket> collection, String file){
        String[] groups = file.trim().split("%");
        for (String group : groups) {
            String[] keyAndProps = group.trim().split("\n");
            String key = keyAndProps[0].split(":")[1].trim();
            String[] properties = keyAndProps[1].split(",");
            Ticket pops = convertToSG(properties);
            collection.put(key, pops);
        }
    }

    private static Ticket convertToSG(String[] props){
        Ticket newGroup = new Ticket();
        for (String param : props){
            String[] parted = param.split(":");

            for (int i = 0; i < parted.length; i++)
                parted[i] = parted[i].trim();

            if (parted.length == 1 || parted[1].equals(""))
                continue;

            switch (parted[0]){
                case "id" -> newGroup.setId(Integer.parseInt(parted[1]));
                case "name" -> newGroup.setName(parted[1].trim());
                case "coordinates" ->{
                    String[] coords = parted[1].split(";");
                    Integer x = Integer.parseInt(coords[0]);
                    float y = Float.parseFloat(coords[1]);
                    newGroup.setCoordinates(new Coordinates());
                }
                case "creationDate" -> {
                    String time = parted[1] + ":" + parted[2] + ":" + parted[3];
                    newGroup.setCreationDate(ZonedDateTime.parse(time));
                }
                case "price" -> newGroup.setPrice(Float.parseFloat(parted[1]));
                case "discount" -> newGroup.setDiscount(Integer.parseInt(parted[1]));
                case "ticket_type" -> newGroup.setType(TicketType.valueOf(parted[1]));
                case "event" -> {
                    Event holidays = new Event();
                    String[] eventProps = parted[1].split(";");
                    holidays.setName(eventProps[0]);
                    holidays.setId(Integer.valueOf(eventProps[1]));
                }
            }
        }
        return newGroup;
    }

    private static String convertToStr(Ticket group, String key){
        StringBuilder converted = new StringBuilder();
        String elemKey = "key: " + key + "\n";
        converted.append(elemKey);
        String[] toFile = new String[9];

        toFile[0] = "id: " + group.getId() + ",";
        toFile[1] = "name: " + group.getName() + ",";
        toFile[2] = "coordinates: " + group.getCoordinates().toString() + ",";
        toFile[3] = "creationDate: " + group.getCreationDate().toString() + ",";
        toFile[4] = "price: " + (group.getPrice()) + ",";
        toFile[5] = "discount: " + (group.getDiscount()) + ",";
        toFile[6] = "ticket_type: " + group.getType().toString() + ",";
        toFile[7] = (group.getEvent() != null) ? "event: " + group.getEvent().toString()
                : "event: ";

        for (String s : toFile) {
            converted.append(s);
        }
        converted.append("\n%\n");
        return converted.toString();
    }

    private static Pair<Boolean, Ticket> tryFind(String name, HashMap<String, Ticket> list){
        for (Map.Entry<String, Ticket> val : list.entrySet()){
            if (val.getValue().getName().equals(name))
                return new Pair<>(true, val.getValue());
        }
        return new Pair<>(false, new Ticket());
    }

    private static List<Ticket> getSublist(Ticket mark, HashMap<String, Ticket> list,
                                           boolean isGreater){
        var groups = new ArrayList<>(list.values());
        Collections.sort(groups);
        int elem = groups.indexOf(mark);
        return (isGreater) ? groups.subList(elem + 1, groups.size()) : groups.subList(0, elem);
    }

    private static void removeRange(String strArg, HashMap<String, Ticket> list, boolean isUp){
        Pair<Boolean, Ticket> funded = tryFind(strArg, list);
        if (funded.first){
            List<Ticket> subList = getSublist(funded.second, list, isUp);
            for (Ticket group : subList) {
                list.values().remove(group);
            }
            print("Элементы " + Arrays.deepToString(subList.toArray()) + " удалены");
        }
        else
            print("Элемент " + strArg + " не найден.");
    }

    private static void print(Object obj){
        System.out.println(obj);
    }
}
