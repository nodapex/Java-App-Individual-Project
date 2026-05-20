package com.bptn.car_management_system.entities;

import com.bptn.car_management_system.interfaces.ISavable;
import java.sql.Connection;

// base entity abstract class that supports other entity class
// Open/Closed Principle - base class open for extension but closed for modification
abstract public class BaseEntity extends BaseDAO implements ISavable {

    protected Long id;
    protected String dateCreated;
    protected String dateUpdated;

    public BaseEntity(){

    };

}
