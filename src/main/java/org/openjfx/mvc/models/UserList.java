package org.openjfx.mvc.models;

import org.openjfx.exceptions.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;

public class UserList implements Serializable {

    private ArrayList<User> users = new ArrayList<>();

    public User getUserByID(int id) throws UserNotFoundException {
        for (User user : this.users) {
            if (user.getId() == id)
                return user;
        }
        throw new UserNotFoundException("User with such id doesn't exist");
    }

    public int push(User user) {
        this.users.add(user);
        return this.users.get(this.users.size() - 1).getId();
    }

    public void setUserById(int id, User user) {
        boolean found = false;
        for (int i = 0; i < this.users.size(); i++) {
            if (this.users.get(i).getId() == id) {
                this.users.set(i, user);
                found = true;
            }
        }
        if (!found) throw new UserIndexOutOfBoundsException("Can't find User with such id");
    }

    /*
     почему-то remove(Object o) выкидывает исключение. в ошибке пишет, что она возникает в эрэйЛисте,
     когда совершается переход на next, при проверке был ли элемент изменен. Это все из-за foreach, думаю.
     Он ведь использует итератор
    */
    public void remove(int id) {
        boolean found = false;
        for (int i = 0; i < this.users.size(); i++) {
            if (this.users.get(i).getId() == id) {
                this.users.remove(i);
                found = true;
            }
        }
        if (!found) throw new UserIndexOutOfBoundsException("Can't find User with such id");
    }

    public void writeFormat (PrintWriter out) throws IOException {
        int i = 0;
        for (User user : users) {
            out.println("User #" + i++);
            user.writeFormat(out);
        }
    }

    public int size() {
        return this.users.size();
    }
}
