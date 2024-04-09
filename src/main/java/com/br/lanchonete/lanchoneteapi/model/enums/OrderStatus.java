package com.br.lanchonete.lanchoneteapi.model.enums;

public enum OrderStatus {
    PENDING{
        @Override
        public OrderStatus nextState(OrderEvents event){
            if(event.equals(OrderEvents.SUCCESS))
                return IN_PROGRESS;
            if(event.equals(OrderEvents.CANCEL))
                return CANCELED;

            return FAILED;
        }
        @Override
        public OrderStatus currentState(){
            return PENDING;
        }
    },
    IN_PROGRESS{
        @Override
        public OrderStatus nextState(OrderEvents event){
            if(event.equals(OrderEvents.SUCCESS))
                return DONE;
            if(event.equals(OrderEvents.CANCEL))
                return CANCELED;

            return FAILED;
        }
        @Override
        public OrderStatus currentState(){
            return IN_PROGRESS;
        }
    },
    DONE{
        @Override
        public OrderStatus nextState(OrderEvents event){
            return DONE;
        }
        @Override
        public OrderStatus currentState(){
            return DONE;
        }
    },
    CANCELED{
        @Override
        public OrderStatus nextState(OrderEvents event){
            return CANCELED;
        }
        @Override
        public OrderStatus currentState(){
            return CANCELED;
        }
    },
    FAILED{
        @Override
        public OrderStatus nextState(OrderEvents event){
            return FAILED;
        }
        @Override
        public OrderStatus currentState(){
            return FAILED;
        }
    };



    public abstract OrderStatus nextState(OrderEvents event);
    public abstract OrderStatus currentState();
}
